package wc_for_fun.pantry_app.pantry_api.domains.items;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wc_for_fun.pantry_app.pantry_api.constants.SourceConstantsInventory;
import wc_for_fun.pantry_app.pantry_api.constants.URIConstantsInventory;
import wc_for_fun.pantry_app.pantry_api.domains.containers.Container;
import wc_for_fun.pantry_app.pantry_api.domains.containers.ContainerService;
import wc_for_fun.pantry_app.pantry_api.mockData.MockData;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ContainerService containerService;

//	@Autowired
//	MockData mockdata;
//	
	@Autowired
	ItemRepo itemRepo;

	@Override
	public Item addItemToValidContainer(Item incomingItem, Container incomingContainer) {
		inferDefaults(incomingItem);
		try {
			itemRepo.save(incomingItem);
		} catch (Exception e) {
			// TODO real error handling.
		}
		incomingContainer.addAnItem(incomingItem);
		incomingItem.getContainers().add(incomingContainer);
		
		return itemRepo.save(incomingItem);
	}

	/**
	 * We're operating under the assumption that a map is going to be more expensive
	 * than traversing the list.
	 * 
	 * @param itemsToUpdate
	 * @param addMeToTheLists
	 * @return
	 */
	private boolean updateContainingInventories(List<Item> itemsToUpdate, Container addMeToTheLists) {
//				List<Container> canIBeUpdated = new ArrayList<>();
//		String containerName = addMeToTheLists.getName();
//		for (Item item : itemsToUpdate) {
//			int lastIndex = item.getContainers().size()-1;
//			if(lastIndex < 0) continue; //don't index oob.
//			if (!item.getContainers().get(lastIndex).getName().equalsIgnoreCase(containerName)) {
//				canIBeUpdated.add(addMeToTheLists);
//				item.getContainers().add(addMeToTheLists);
//			}
//		}
//		return true;
		for (Item item : itemsToUpdate) {
			boolean doAdd = true;
			List<Container> containersHoldingItem = item.getContainers();
			if (containersHoldingItem == null) {
				containersHoldingItem = new ArrayList<>();
			}
			if (containersHoldingItem.size() == 0) {
				System.out.println("maybe I want to do stuff here?");
			} else {
				String addMeName = addMeToTheLists.getName();
				for (Container container : item.getContainers()) {
					if (addMeName.equalsIgnoreCase(container.getName())) {
						doAdd = false;
						break;
					}
				}
			}
			if (doAdd) {
				try {
					containersHoldingItem.add(addMeToTheLists);
				} catch (UnsupportedOperationException e) {
					containersHoldingItem = new ArrayList<Container>(containersHoldingItem);
					containersHoldingItem.add(addMeToTheLists);
				}
				item.setContainers(containersHoldingItem);
			}
		}
		return true;
	}

	@Override
	public List<Item> getAll() {
		return itemRepo.findAll();
		// return mockdata.getItems();
	}

	@Override
	public Item getSolo(Long itemId) {
		try {
			Optional<Item> retrievedItem = itemRepo.findById(itemId);
			return retrievedItem.orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	/**
	 * @param incomingUPC this method relies on this being well-formed.
	 */
	public ItemAndSourceDTO getItemByUPC(String incomingUPC) {
		Item tentativeItem = null;
		UPCWrapper upcWrapper = new UPCWrapper(incomingUPC);
		List<Item> retrievedItems = itemRepo.findAllByUpc(upcWrapper);
		if (retrievedItems.size() > 0) //altered from == 1 because current model forbids uniqueness. TODO fix that.
			tentativeItem = retrievedItems.get(0);
		if (tentativeItem != null) {
			ItemAndSourceDTO readyToReturn = new ItemAndSourceDTO();
			readyToReturn.setItem(tentativeItem);
			readyToReturn.setSource(SourceConstantsInventory.CACHE);
			return readyToReturn;
		}
		HttpUrl.Builder urlBuilder = HttpUrl.parse(URIConstantsInventory.DEFAULT_UPC_LOOKUP_URL).newBuilder();
		urlBuilder.addQueryParameter(URIConstantsInventory.DEFAULT_UPC_LOOKUP_QUERY_ARG_0, incomingUPC);

		String url = urlBuilder.build().toString();

		Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/json").build();

		OkHttpClient client = new OkHttpClient.Builder().build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				ItemAndSourceDTO parseAndReturn = new ItemAndSourceDTO();
				parseAndReturn.setSource(SourceConstantsInventory.DEFAULT_UPC_LOOKUP);
				Item fromUPC = convertResponseToItem(response);
				parseAndReturn.setItem(fromUPC);
				return parseAndReturn;
			}
		} catch (IOException e) {
			System.out.println("IO exception logging");
		}
		// if you got here, it's a 404.
		ItemAndSourceDTO fourOfour = new ItemAndSourceDTO();
		fourOfour.setSource(SourceConstantsInventory.NO_SOURCE);
		return fourOfour;
	}

//	@Override
//	public boolean hasItemByUPC(String incomingUPC) {
//		for (Item item : mockdata.getItems())
//			if (item.getUpc().equals(incomingUPC))
//				return true;
//		return false;
//	}

	@Override
	public List<Item> getItemsByUPC(String incomingUPC) {
		return itemRepo.findByUpcString(incomingUPC);
	}

	private Item convertResponseToItem(Response incomingResponse) {
		ObjectMapper autowireThisMapper = new ObjectMapper();
		try {
			JsonNode responseRoot = autowireThisMapper.readTree(incomingResponse.body().byteStream());
			JsonNode itemNode = responseRoot.findValues("items").get(0);
			Item takingJsonValues = new Item();
			takingJsonValues.setUpc(new UPCWrapper(itemNode.findValue("upc").asText()));
			takingJsonValues.setNaiiveItemName(itemNode.findValue("title").asText());
			takingJsonValues.setNaiiveItemDescription(itemNode.findValue("description").asText());
			return takingJsonValues;
		} catch (IOException e) {
			System.out.println("Log jackson fail?");
			return null;
		}
	}

	@Override
	public Item inferDefaults(Item incomingItem) {
		if (incomingItem.getContainers() == null)
			incomingItem.setContainers(new ArrayList<>());
		if (incomingItem.getObtainDate() == null)
			incomingItem.setObtainDate(LocalDate.now());
		if (incomingItem.getExpiryDate() == null)
			incomingItem.setExpiryDate(incomingItem.getObtainDate().plusDays(5));
		return incomingItem;
	}

//	private Optional<Item> loadFromRepo(Long getById){
//		try {
//		return itemRepo.findById(getById);
//		} catch(Exception e) {
//			System.out.println("We hit a catch in service");
//		}
//		return Optional.empty();
//	}

	@Override
	public Item updateTargetEntityWithPassedModel(Long targetId, Item incomingItem) {
		Item existingItem = getSolo(targetId);
		if(existingItem==null) return null;
		if(!existingItem.getId().equals(incomingItem.getId())) {
			System.out.println("Id discrepancy on PUT");
			incomingItem.setId(Long.valueOf(0));
			return incomingItem;
		}
		existingItem.updatePropertiesFromItem(incomingItem); //this is a lot of work just to transfer a bag over.
		return itemRepo.save(existingItem);
	}

	@Override
	public Item deleteItemById(Long targetId) {
		Item existingItem = getSolo(targetId);
		if(existingItem==null) return null;
		itemRepo.deleteById(targetId);
		return existingItem;
	}

}
