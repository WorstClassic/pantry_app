package wc_for_fun.pantry_app.redirect;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class Redirect {
	
	private final String server = "localhost";
	private final int port = 8081;
	
	@RequestMapping("/api/**")
	/**
	 * For further reference: https://stackoverflow.com/questions/14726082/spring-mvc-rest-service-redirect-forward-proxy
	 * @param body
	 * @param method
	 * @param request
	 * @param response
	 * @return
	 * @throws URISyntaxException
	 */
	public ResponseEntity mirrorRest(@RequestBody(required = false) String body, 
	    HttpMethod method, HttpServletRequest request, HttpServletResponse response) 
	    throws URISyntaxException {
	    String requestUrl = request.getRequestURI().replace("/pantry_app/api/", "/");

	    URI uri = new URI("http", null, server, port, null, null, null);
	    uri = UriComponentsBuilder.fromUri(uri)
	                              .path(requestUrl)
	                              .query(request.getQueryString())
	                              .build(true).toUri();

	    HttpHeaders headers = new HttpHeaders();
	    Enumeration<String> headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String headerName = headerNames.nextElement();
	        headers.set(headerName, request.getHeader(headerName));
	    }

	    HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
	    RestTemplate restTemplate = new RestTemplate();
	    try {
	        return restTemplate.exchange(uri, method, httpEntity, String.class);
	    } catch(HttpStatusCodeException e) {
	        return ResponseEntity.status(e.getRawStatusCode())
	                             .headers(e.getResponseHeaders())
	                             .body(e.getResponseBodyAsString());
	    }
	}
}
