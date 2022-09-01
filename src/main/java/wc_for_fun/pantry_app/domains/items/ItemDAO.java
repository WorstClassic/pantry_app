package wc_for_fun.pantry_app.domains.items;

import java.util.List;
import java.util.Optional;

public interface ItemDAO {
	public List<Item> getItems();
	public Optional<Item> findById(Long idOfItem);
	public List<Item> findAllByUpc(String upc);
	public Optional<Item> saveItem(Item newItem);
	public Optional<Item> updateItem(Item updatedItem);
	public Optional<Item> deleteItem(Long idOfItem);
}
