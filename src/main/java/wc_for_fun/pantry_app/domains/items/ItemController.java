package wc_for_fun.pantry_app.domains.items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wc_for_fun.pantry_app.constants.SourceConstantsInventory;

@RestController
@RequestMapping(value = "/api/items")
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