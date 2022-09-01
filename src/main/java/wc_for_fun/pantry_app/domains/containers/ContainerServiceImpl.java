package wc_for_fun.pantry_app.domains.containers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wc_for_fun.pantry_app.domains.items.Item;
import wc_for_fun.pantry_app.mockData.MockData;

@Service
public class ContainerServiceImpl implements ContainerService {

//	@Autowired
//	MockData mockdata;

	@Autowired
	ContainerDAO containerRepo;

	@Override
	public Container addContainer(Container incomingContainer) {
		Optional<Container> tentativeReturn = containerRepo.saveContainer(incomingContainer);
		return tentativeReturn.orElse(null);
	}

	/**
	 * In this implementation, the service is confirming that the incoming container
	 * exists on the item's containers list, and updating the existing
	 * back-reference on itself.
	 */
	@Override
	public Item addItemToContainer(Item incomingItem, Container incomingContainer) {
		incomingContainer.addAnItem(incomingItem);
		Container savedContainer = containerRepo.updateContainer(incomingContainer).get();
		return incomingItem;
//			if (incomingItem.getContainers().contains(incomingContainer)) {
//			} else {
//			System.out.println(
//					"We didn't correctly back-reference the container and I don't want to create responsibility confusion.");
//		}
//			return null;
	}

	@Override
	public Container getSolo(Long containerId) {
		return containerRepo.getContainerById(containerId).orElse(null);
	}

	@Override
	public List<Container> getAll(String name) {
		if (name == null || name.isEmpty())
			return containerRepo.getContainers();
		return containerRepo.getContainersByName(name);
	}

//	@Override
//	/**
//	 * Call relies on valid name on queryContainer
//	 * 
//	 */
//	public boolean containerExistsByName(Container queryContainer) {
//		for (Container element : mockdata.getContainers()) {
//			if (queryContainer.getName().equalsIgnoreCase(element.getName()))
//				return true;
//		}
//		return false;
//	}
//
//	public boolean containerExistsByName(String queryString) {
//		for (Container element : mockdata.getContainers()) {
//			if (queryString.equalsIgnoreCase(element.getName()))
//				return true;
//		}
//		return false;
//	}

	@Override
	public Container getContainerByName(String queryString) {
		List<Container> retrievedResults = containerRepo.getContainersByName(queryString);
		if (retrievedResults.isEmpty())
			return null;
		if (retrievedResults.size() != 1)
			return null; // Collision handling later.
		return retrievedResults.get(0);
	}

	@Override
	public Container updateTargetEntityWithPassedModel(Long targetId, Container incomingContainer) {
		Container existingContainer = getSolo(targetId);
		if (existingContainer == null)
			return null;
		if (!existingContainer.getId().equals(incomingContainer.getId())) {
			System.out.println("Id discrepancy on PUT");
			incomingContainer.setId(Long.valueOf(0));
			return incomingContainer;
		}
		try {
			existingContainer.updateProperties(incomingContainer);
			return containerRepo.updateContainer(existingContainer).get();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Container deleteContainerById(Long targetId) {
		Container existingContainer = getSolo(targetId);
		if (existingContainer == null)
			return null;
		return containerRepo.deleteContainer(targetId).get();
	}

}
