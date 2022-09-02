package wc_for_fun.pantry_app.pantry_api.domains.items;

import java.util.List;

public interface ItemDao {
public List<Item> findByUpcString(String upc);
}
