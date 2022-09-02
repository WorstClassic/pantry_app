package wc_for_fun.pantry_app.viewControllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Controller
public class HomeController {

	@RequestMapping(value = "/")
	public ModelAndView test(HttpServletResponse response) throws IOException {
		return new ModelAndView("home");
	}

	@RequestMapping(value = "inventory")
	public String getInventory(ModelMap model) {
		return "inventory_page";
	}

	@RequestMapping(value = "inventory/{id}")
	public String getItemEdit(ModelMap model) {
		return "edit_item_page";
	}

	@RequestMapping(value = "containers")
	public String getContainers(ModelMap model) {
		return "container_page";
	}

	@RequestMapping(value = "containers/{id}/new-item")
	public String getAddPage(@PathVariable("id") String idString,
			// @ModelAttribute("containers") ArrayList<Container> currentContainers,
			// Eliding this by going straight to mock data.
			ModelMap model) {
		Long idAsLong = Long.MIN_VALUE;
		try {
			idAsLong = Long.parseUnsignedLong(idString);
		} catch (Exception e) {
			// Probably wiser just to return errorpage here.
		}
		if (idAsLong.equals(Long.MIN_VALUE)) {
			return "error_page";
		}
		String containerName = retrieveContainerName(idAsLong);
		if (containerName != null && !containerName.isEmpty()) {
			model.addAttribute("containerName", containerName);
			return "add_item_page";
		} else {
			model.addAttribute("add_error", "Attempted to add to a container that doesn't seem to exist.");
			return "error_page";
		}
	}

	private String retrieveContainerName(Long id) {

		Request request = new Request.Builder().url("http://localhost:8080/containers/" + id.toString()).build();

		OkHttpClient client = new OkHttpClient.Builder().build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				String returnedName = null;
				ObjectMapper autowireThisMapper = new ObjectMapper();
				try {
					JsonNode responseRoot = autowireThisMapper.readTree(response.body().byteStream());
					returnedName = responseRoot.findValue("name").asText();
				} catch (IOException e) {
					// System.out.println("Log jackson fail?");
					// return null;
				}
				response.close();
				return returnedName;
			} else {
				response.close();
				System.out.println("Pin here for debugging");
			}
		} catch (IOException e) {
			System.out.println("IO exception logging");
		} finally {
		}
		return null;
	}

	@RequestMapping(value = "containers", method = RequestMethod.POST)
	public String postContainerRedirect(@ModelAttribute("newContainer") String incomingContainer, ModelMap model) {

		if (incomingContainer != null) {
			if (!incomingContainer.isEmpty()) {
				String containerString = "{\r\n" + "    \"name\":\"" + incomingContainer + "\",\r\n"
						+ "    \"contents\":[]\r\n" + "}";
				okhttp3.RequestBody newContainerAsJSON = okhttp3.RequestBody.create(containerString,
						MediaType.get("application/json"));
				Request request = new Request.Builder().url("http://localhost:8080/containers").post(newContainerAsJSON)
						.build();

				OkHttpClient client = new OkHttpClient.Builder().build();

				Call call = client.newCall(request);
				try {
					Response response = call.execute();
					if (response.isSuccessful()) {
						String returnedId = null;
						ObjectMapper autowireThisMapper = new ObjectMapper();
						try {
							JsonNode responseRoot = autowireThisMapper.readTree(response.body().byteStream());
							returnedId = responseRoot.findValue("id").asText();
						} catch (IOException e) {
							// System.out.println("Log jackson fail?");
							// return null;
						}
						response.close();
						if (returnedId == null)
							return "container_page";
						model.remove("error");
						model.remove("newContainer");
						return "redirect:/containers/" + returnedId + "/new-item";
					} else {
						response.close();
						System.out.println("Pin here for debugging");
					}
				} catch (IOException e) {
					System.out.println("IO exception logging");
				} finally {
				}

			}
		}
		// model.addAttribute("openAddContainerRequest","true");
		model.addAttribute("error", "Please enter a unique name.");
		return "container_page";
	}
}
