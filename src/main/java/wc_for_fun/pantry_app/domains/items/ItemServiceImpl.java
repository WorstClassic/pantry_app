package wc_for_fun.pantry_app.domains.items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wc_for_fun.pantry_app.constants.SourceConstantsInventory;
import wc_for_fun.pantry_app.constants.URIConstantsInventory;
import wc_for_fun.pantry_app.domains.containers.Container;
import wc_for_fun.pantry_app.domains.containers.ContainerService;
import wc_for_fun.pantry_app.mockData.MockData;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	ContainerService containerService;

	@Autowired
	MockData mockdata;

	@Override
	public Item addItemToContainer(Item incomingItem, Container incomingContainer) {
		if (incomingItem != null) {
			Container containerReference = null;
			if (incomingContainer != null) {
				String queryString = incomingContainer.getName();
				if (queryString == null || queryString.isEmpty()
						|| !containerService.containerExistsByName(queryString)) {
					// TODO with real logging.
					System.out.println("Container unposted");
					return null; // break due to un-posted container.
				}
				containerReference = containerService.getContainerByName(queryString);
			}
			UPCWrapper incomingUPC = incomingItem.getUpc();
			if (incomingUPC == null)
				return null; // break due to invalid incoming item.
			String incomingUPCString = incomingUPC.toString();
			if (hasItemByUPC(incomingUPCString)) { // item has at least one listing.
				List<Item> listOfExistingItems = getItemsByUPC(incomingUPCString);
				List<Container> clonedList = new ArrayList<>();
				clonedList.addAll(listOfExistingItems.get(0).getContainers());
				listOfExistingItems.add(incomingItem);
				if (updateContainingInventories(listOfExistingItems, containerReference)) {
					// TODO log success?
					System.out.println("Successful add of item to inventory.");
				}
			} else { // case no current listings.
				ArrayList<Container> cleanContainerInventory = new ArrayList<>();
				cleanContainerInventory.add(incomingContainer);
				incomingItem.setContainers(cleanContainerInventory);
			}
			containerService.addItemToContainer(incomingItem, incomingContainer);
			mockdata.getItems().add(incomingItem);
			return incomingItem;
		}
		return null;
	}

	private boolean updateContainingInventories(List<Item> itemsToUpdate, Container addMeToTheLists) {
		for (Item item : itemsToUpdate) {
			int lastIndex = item.getContainers().size();
			if (!item.getContainers().get(lastIndex).getName().equalsIgnoreCase(addMeToTheLists.getName()))
				item.getContainers().add(addMeToTheLists);
		}
		return true;
	}

	@Override
	public List<Item> getAll() {
		return mockdata.getItems();
	}

	@Override
	public Item getSolo(Long itemId) {
		try {
			return mockdata.getItems().get(itemId.intValue());
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	/**
	 * @param incomingUPC this method relies on this being well-formed.
	 */
	public ItemAndSourceDTO getItemByUPC(String incomingUPC) {
		Item tentativeItem = null;
		for (Item item : mockdata.getItems())
			if (item.getUpc().toString().equalsIgnoreCase(incomingUPC)) {
				tentativeItem = item;
				break;
			}
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

	@Override
	public boolean hasItemByUPC(String incomingUPC) {
		for (Item item : mockdata.getItems())
			if (item.getUpc().toString().equalsIgnoreCase(incomingUPC))
				return true;
		return false;
	}

	@Override
	public List<Item> getItemsByUPC(String incomingUPC) {
		ArrayList<Item> returnList = new ArrayList<>();
		for (Item item : mockdata.getItems())
			if (item.getUpc().toString().equalsIgnoreCase(incomingUPC))
				if (!returnList.add(item)) {
					// TODO clean this loggish stub.
					System.out.println("Something weird happened on add to list");
				}
		return returnList;
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
		}
		catch(IOException e) {
			System.out.println("Log jackson fail?");
			return null;
		}
	}

}
