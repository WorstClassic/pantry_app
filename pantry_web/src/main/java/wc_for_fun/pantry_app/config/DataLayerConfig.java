//package wc_for_fun.pantry_app.config;
//
//import java.util.Properties;
//
//import javax.sql.DataSource;
//
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement
//public class DataLayerConfig {
//
//	@Bean
//	public LocalSessionFactoryBean getSessionFactory() {
//		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//		sessionFactory.setDataSource(dataSource());
//		sessionFactory.setPackagesToScan("wc_for_fun.pantry_app");
//		sessionFactory.setHibernateProperties(hibernateProperties());
//		return sessionFactory;
//	}
//
//	@Bean
//	public DataSource dataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("org.h2.Driver");
//		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//		dataSource.setUsername("admin");
//		dataSource.setPassword("admin");
////		
////		Properties properties = new Properties();
////		properties.put("hibernate.hbm2dll.auto", "create");
////		
////		dataSource.setConnectionProperties(properties);
////		
//		return dataSource;
////		return new EmbeddedDatabaseBuilder()
////				.generateUniqueName(false)
////				.setName("testdb")
////				.setType(EmbeddedDatabaseType.H2)
////				//.addDefaultScripts()
////				//.addScriptEncoding("UTF-8")
////				.ignoreFailedDrops(true)
////				.build();
//	}
//
//	@Bean
//	@Autowired
//	public HibernateTransactionManager hibernateTransactionManager(SessionFactory s) {
//		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//		transactionManager.setSessionFactory(s);
//		return transactionManager;
//	}
//
//	private final Properties hibernateProperties() {
//		Properties hibernateProperties = new Properties();
//		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//		hibernateProperties.setProperty("hibernate.show_sql", "true");
//		
//		hibernateProperties.setProperty("javax.persistence.schema-generation.database.action","drop-and-create");
//		return hibernateProperties;
//	}
//}
