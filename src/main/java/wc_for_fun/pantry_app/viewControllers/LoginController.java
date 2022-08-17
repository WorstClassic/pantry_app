//package wc_for_fun.pantry_app.viewControllers;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//
//@Controller
//public class LoginController {
//	
//	@GetMapping("/")
//	public String getMain(ModelMap model) {
//		if(model.containsAttribute("user"))
//			return "home";
//		return "login_page";
//	}
//
//	@GetMapping(value = "/login")
//	public String getlogin_page() {
//		return "login_page";
//	}
//	
//	@GetMapping(value = "/logout")
//	public String logoutAction(ModelMap model) {
//		model.remove("user");
//		return "login_page";
//	}
//
//	@PostMapping(value = "/login")
//	public String login(@ModelAttribute("login") LoginDTO login, ModelMap model) {
//		if (login != null) {
//			boolean okForService = true;
//			if (login.getUsername() == null) {
//				okForService = false;
//				model.addAttribute("username_error", "This cannot be empty");
//			}
//			if (login.getPassword() == null) {
//				okForService = false;
//				model.addAttribute("password_error", "This cannot be empty");
//			}
//			if(okForService) {
//				if(login.getUsername().isEmpty())
//				{
//					model.addAttribute("username_error", "This cannot be empty");
//				}
//				model.addAttribute("user", login.getUsername());
//				return "home";
//			}
//			
//		}
//		return "login_page";
//	}
//}
