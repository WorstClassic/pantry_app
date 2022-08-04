package wc_for_fun.pantry_app.viewControllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value="/")
	public ModelAndView test(HttpServletResponse response) throws IOException{
		return new ModelAndView("home");
	}
	
	@RequestMapping(value="inventory")
	public String getInventory(ModelMap model) {
		return "inventory_page";
	}
	@RequestMapping(value="containers")
	public String getContainers(ModelMap model) {
		return "container_page";
	}
}
