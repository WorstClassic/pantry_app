package domains.items;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domains.containers.Container;
import domains.containers.ContainerService;
import mockData.MockData;

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
					//TODO log success?
					System.out.println("Successful add of item to inventory.");
				}
			} else { //case no current listings. 
				ArrayList<Container> cleanContainerInventory = new ArrayList<>();
				cleanContainerInventory.add(incomingContainer);
				incomingItem.setContainers(cleanContainerInventory);
			}
			containerService.addItemToContainer(incomingItem, incomingContainer);
			MockData.getItems().add(incomingItem);
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
		return MockData.getItems();
	}

	@Override
	public Item getSolo(Long itemId) {
		return MockData.getItems().get(itemId.intValue());
	}

	@Override
	/**
	 * @param incomingUPC this method relies on this being well-formed.
	 */
	public Item getItemByUPC(String incomingUPC) {
		for (Item item : MockData.getItems())
			if (item.getUpc().toString().equalsIgnoreCase(incomingUPC))
				return item;
		return null;
	}

	@Override
	public boolean hasItemByUPC(String incomingUPC) {
		for (Item item : MockData.getItems())
			if (item.getUpc().toString().equalsIgnoreCase(incomingUPC))
				return true;
		return false;
	}

	@Override
	public List<Item> getItemsByUPC(String incomingUPC) {
		ArrayList<Item> returnList = new ArrayList<>();
		for (Item item : MockData.getItems())
			if (item.getUpc().toString().equalsIgnoreCase(incomingUPC))
				if (!returnList.add(item)) {
					// TODO clean this loggish stub.
					System.out.println("Something weird happened on add to list");
				}
		return returnList;
	}

}
