package domains.containers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domains.items.Item;
import mockData.MockData;

@Service
public class ContainerServiceImpl implements ContainerService {
	
	@Autowired
	MockData mockData;

	@Override
	public Container addContainer(Container incomingContainer) {
		mockData.getContainers().add(incomingContainer);
		return incomingContainer;
	}

	@Override
	public Item addItemToContainer(Item incomingItem, Container incomingContainer) {
		if (incomingContainer.addAnItem(incomingItem))
			if (incomingItem.getContainers().contains(incomingContainer))
				if (incomingItem.getContainers().add(incomingContainer))
					return incomingItem;
		return null;
	}

	@Override
	public Container getSolo(Long containerId) {
		try {
			return mockData.getContainers().get(containerId.intValue());
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Out of bounds Id.");
		}
		return null;
	}

	@Override
	public List<Container> getAll() {
		return mockData.getContainers();
	}

	@Override
	/**
	 * Call relies on valid name on queryContainer
	 * 
	 */
	public boolean containerExistsByName(Container queryContainer) {
		for (Container element : mockData.getContainers()) {
			if (queryContainer.getName().equalsIgnoreCase(element.getName()))
				return true;
		}
		return false;
	}

	public boolean containerExistsByName(String queryString) {
		for (Container element : mockData.getContainers()) {
			if (queryString.equalsIgnoreCase(element.getName()))
				return true;
		}
		return false;
	}

	@Override
	public Container getContainerByName(String queryString) {
		for (Container element : mockData.getContainers()) {
			if (queryString.equalsIgnoreCase(element.getName()))
				return element;
		}
		return null;
	}

}
