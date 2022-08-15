package wc_for_fun.pantry_app.domains.containers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wc_for_fun.pantry_app.domains.items.Item;
import wc_for_fun.pantry_app.mockData.MockData;

@Service
public class ContainerServiceImpl implements ContainerService {

	@Autowired
	MockData mockdata;

	@Override
	public Container addContainer(Container incomingContainer) {
		incomingContainer.setId(Long.valueOf(mockdata.getContainers().size()));
		mockdata.getContainers().add(incomingContainer);
		return incomingContainer;
	}

	/**
	 * In this implementation, the service is confirming that the incoming container
	 * exists on the item's containers list, and updating the existing
	 * back-reference on itself.
	 */
	@Override
	public Item addItemToContainer(Item incomingItem, Container incomingContainer) {
		if (incomingContainer.addAnItem(incomingItem))
			if (incomingItem.getContainers().contains(incomingContainer)) {
				return incomingItem;
			} else {
				System.out.println("We didn't correctly back-reference the container and I don't want to create responsibility confusion.");
			}
		return null;
	}

	@Override
	public Container getSolo(Long containerId) {
		try {
			return mockdata.getContainers().get(containerId.intValue());
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Out of bounds Id.");
		}
		return null;
	}

	@Override
	public List<Container> getAll() {
		return mockdata.getContainers();
	}

	@Override
	/**
	 * Call relies on valid name on queryContainer
	 * 
	 */
	public boolean containerExistsByName(Container queryContainer) {
		for (Container element : mockdata.getContainers()) {
			if (queryContainer.getName().equalsIgnoreCase(element.getName()))
				return true;
		}
		return false;
	}

	public boolean containerExistsByName(String queryString) {
		for (Container element : mockdata.getContainers()) {
			if (queryString.equalsIgnoreCase(element.getName()))
				return true;
		}
		return false;
	}

	@Override
	public Container getContainerByName(String queryString) {
		for (Container element : mockdata.getContainers()) {
			if (queryString.equalsIgnoreCase(element.getName()))
				return element;
		}
		return null;
	}

}
