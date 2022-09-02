package wc_for_fun.pantry_app.pantry_api.domains.items;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long>, ItemDao {
	public List<Item> findAll();
	public Optional<Item> findById(Long idOfItem);
	
	public List<Item> findAllByUpc(UPCWrapper upc);
	//Now handled in ItemDAO
}
