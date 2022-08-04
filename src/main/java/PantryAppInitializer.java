//
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRegistration;
//
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import wc_for_fun.pantry_app.config.MvcConfiguration;
//
//public class PantryAppInitializer implements WebApplicationInitializer {
//
//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
//		webContext.setConfigLocation("wc_for_fun.panty_app.config");
//		
//		webContext.register(MvcConfiguration.class);
//		webContext.setServletContext(servletContext);
//
//		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher5", new DispatcherServlet(webContext));
//
//		servlet.setLoadOnStartup(1);
//		servlet.addMapping("/");
//
//	}
//
//}
