package domains.items;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mockData.MockData;

@RestController
@RequestMapping(value = "/api/items")
public class ItemController {
	
	@Autowired
	ItemService itemService;

	@GetMapping
	public ResponseEntity<List<Item>> getAllUserItems(){//@RequestHeader(HttpHeaders.AUTHORIZATION) String username) {
//		if (username == null || username.isEmpty())
//			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(itemService.getAll(), HttpStatus.OK);
	}
}