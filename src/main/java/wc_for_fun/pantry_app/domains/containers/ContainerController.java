package wc_for_fun.pantry_app.domains.containers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wc_for_fun.pantry_app.domains.items.Item;
import wc_for_fun.pantry_app.domains.items.ItemService;

@RestController
@RequestMapping(value = "api/containers")
public class ContainerController {

	@Autowired
	ContainerService containerService;
	@Autowired
	ItemService itemService;

	@GetMapping()
	public ResponseEntity<List<Container>> getAllUserItems() {
		return new ResponseEntity<>(containerService.getAll(), HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Container> getSolo(@PathVariable Long id) {
		Container tentativeReturn = containerService.getSolo(id);
		if (tentativeReturn == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(tentativeReturn, HttpStatus.OK);
	}

	@PostMapping(consumes = "application/json" )
	public ResponseEntity<Container> postContainer(@RequestBody Container newContainer) {
		return new ResponseEntity<>(containerService.addContainer(newContainer), HttpStatus.CREATED);
	}

	@PostMapping(value = "/{id}/items")
	public ResponseEntity<Item> postItemInContainer(@PathVariable Long id, @RequestBody Item newItem) {
		Container existingContainer = containerService.getSolo(id);
		if (existingContainer == null)
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		Item tentativeReturn = itemService.addItemToValidContainer(newItem, existingContainer);
		tentativeReturn = containerService.addItemToContainer(tentativeReturn, existingContainer);
		if (tentativeReturn == null)
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(tentativeReturn, HttpStatus.CREATED);
	}

}
