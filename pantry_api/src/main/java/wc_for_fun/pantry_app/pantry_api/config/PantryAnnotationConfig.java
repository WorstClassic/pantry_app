//package wc_for_fun.pantry_app.pantry_api.config;
//
//import javax.servlet.Filter;
//
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//import wc_for_fun.pantry_app.security.CORSElider;
//
//public class PantryAnnotationConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//	@Override
//	protected Class<?>[] getRootConfigClasses() {
//		return new Class[] { MvcConfiguration.class };
//	}
//
//	@Override
//	protected Class<?>[] getServletConfigClasses() {
//		return null;
//	}
//
//	@Override
//	protected String[] getServletMappings() {
//		return new String[] { "/" };
//	}
//	
//	@Override
//	protected Filter[] getServletFilters() {
//		Filter [] singleton = { new CORSElider() };
//		return singleton;
//	}
//
//}
