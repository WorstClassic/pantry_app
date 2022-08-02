/**
 * 
 */
package domains.items;

import java.util.List;

import domains.containers.Container;

/**
 * @author Classic
 *
 */
public interface ItemService {
	public List<Item> getAll();
	public Item getSolo(Long itemId);
	public Item addItemToContainer(Item incomingItem, Container incomingContainer);
	public boolean hasItemByUPC(String incomingUPC);
	public Item getItemByUPC(String incomingUPC);
	public List<Item> getItemsByUPC(String incomingUPC);
}
