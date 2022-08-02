package domains.containers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mockData.MockData;

@RestController
@RequestMapping(value="api/containers")
public class ContainerController {
	
	@Autowired
	ContainerService containerService;
	
	@GetMapping()
	public ResponseEntity<List<Container>> getAllUserItems(){//@RequestHeader(HttpHeaders.AUTHORIZATION) String username) {
		//if (username == null || username.isEmpty())
//			return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
		return new ResponseEntity<>(containerService.getAll(), HttpStatus.OK);
	}
}
