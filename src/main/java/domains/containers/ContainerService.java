/**
 * 
 */
package domains.containers;

import java.util.List;

import domains.items.Item;

/**
 * @author Classic
 *
 */
public interface ContainerService {
	
	public Container getSolo(Long containerId);
	
	public List<Container> getAll();
	
	public Container addContainer(Container incomingContainer);

	public Item addItemToContainer(Item incomingItem, Container incomingContainer);
	
	public boolean containerExistsByName(Container queryContainer);
	public boolean containerExistsByName(String queryString);
	
	public Container getContainerByName(String queryString);
}
