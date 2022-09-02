package wc_for_fun.pantry_app.pantry_api.domains.items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wc_for_fun.pantry_app.pantry_api.constants.SourceConstantsInventory;

@RestController
@CrossOrigin(origins={"http://localhost:3000","https://localhost:3000",
		"http://localhost:8080", "https://localhost:8080"})
@RequestMapping(value = "/items")
public class ItemController {

	@Autowired
	ItemService itemService;

	@GetMapping
	public ResponseEntity<List<Item>> getAllUserItems() {
		return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Item tentativeReturn = itemService.getSolo(id);
		if (tentativeReturn == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(tentativeReturn, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item updateInfo)
	{
		Item tentativeReturn = null;
		
		tentativeReturn=itemService.updateTargetEntityWithPassedModel(id, updateInfo);
		if(tentativeReturn==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if(tentativeReturn.getId()==null || tentativeReturn.getId().equals(Long.valueOf(0)))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(tentativeReturn, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable Long id)
	{
		Item tentativeReturn = null;
		tentativeReturn = itemService.deleteItemById(id);
		if(tentativeReturn==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/upc/{upc}")
	public ResponseEntity<ItemAndSourceDTO> getItemsByUpc(@PathVariable String upc) {
		ItemAndSourceDTO tentativeReturn = itemService.getItemByUPC(upc);
		if (tentativeReturn == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		if (tentativeReturn.size() == 0)
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		if (SourceConstantsInventory.NO_SOURCE.equals(tentativeReturn.getSource()))
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(tentativeReturn, HttpStatus.OK);
	}
}