/**
 * 
 */
package wc_for_fun.pantry_app.domains.containers;

import java.util.List;

import wc_for_fun.pantry_app.domains.items.Item;

/**
 * @author Classic
 *
 */
public interface ContainerService {
	
	public Container getSolo(Long containerId);
	public Container updateTargetEntityWithPassedModel(Long targetId, Container incomingContainer);
	public Container deleteContainerById(Long targetId);
	
	public List<Container> getAll(String name);
	
	public Container addContainer(Container incomingContainer);

	public Item addItemToContainer(Item incomingItem, Container incomingContainer);
	
	//public boolean containerExistsByName(Container queryContainer);
	//return nupublic boolean containerExistsByName(String queryString);
	
	public Container getContainerByName(String queryString);
}
