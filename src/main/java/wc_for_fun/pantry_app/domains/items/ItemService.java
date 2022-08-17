/**
 * 
 */
package wc_for_fun.pantry_app.domains.items;

import java.util.List;

import wc_for_fun.pantry_app.domains.containers.Container;

/**
 * @author Classic
 *
 */
public interface ItemService {
	public List<Item> getAll();
	public Item getSolo(Long itemId);
	public Item addItemToValidContainer(Item incomingItem, Container incomingContainer);
	public boolean hasItemByUPC(String incomingUPC);
	public ItemAndSourceDTO getItemByUPC(String incomingUPC);
	public List<Item> getItemsByUPC(String incomingUPC);
}
