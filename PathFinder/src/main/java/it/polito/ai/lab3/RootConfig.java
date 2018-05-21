package it.polito.ai.lab3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.MongoClient;

import it.polito.ai.lab3.mongo.repo.MinPathsRepository;
import it.polito.ai.lab3.services.buslinesviewer.BusLinesViewerService;
import it.polito.ai.lab3.services.buslinesviewer.BusLinesViewerServiceImpl;
import it.polito.ai.lab3.services.routing.RoutingService;
import it.polito.ai.lab3.services.routing.RoutingServiceImpl;

@Configuration // Indica che la classe ha lo scopo di definire bean
@EnableMongoRepositories({"it.polito.ai.lab3.mongo.repo"}) // Supporto per MongoDB
@EnableJpaRepositories(basePackages = "it.polito.ai.lab3.repo") // Supporto per ORM conforme a JPA
@ComponentScan(basePackages={"it.polito.ai.lab3.service"}) // Indica dove in quale package vanno cercati i componenti
@EnableTransactionManagement
public class RootConfig {

	@Bean
	public RoutingService routingService(){
		return new RoutingServiceImpl();
	}

	@Bean 
	public BusLinesViewerService busLinesViewerService() {
		return new BusLinesViewerServiceImpl();
	}

	// TODO this should not be there, autowiring should discover it
	@Bean 
	public MinPathsRepository minPathsRepository() {
		return new MinPathsRepository();
	}

	@Bean
	public DataSource dataSource() {
		String server = null;
		try {
			List<String> lines = Files.readAllLines(Paths.get(
					RootConfig.class.getClassLoader().getResource("db_ip.txt").toURI().toString().substring(6)));
			server = lines.get(0);
		} catch (Exception e) {
			server = "localhost";
		}


		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUrl("jdbc:postgresql://" + server + ":5432/trasporti");
		ds.setUsername("postgres");
		ds.setPassword("ai-user-password");
		return ds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.POSTGRESQL);
		vendorAdapter.setGenerateDdl(false);
		vendorAdapter.setShowSql(false);
		vendorAdapter.setDatabasePlatform("org.hibernate.spatial.dialect.postgis.PostgisDialect");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("it.polito.ai.lab3.repo.entities");
		factory.setDataSource(dataSource);
		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory factory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(factory);
		return txManager;
	}

	@Bean
	public MongoDbFactory mongoDbFactory() throws Exception {	
		String databaseName = "ai";
		String server;
		try {
			List<String> lines = Files.readAllLines(Paths.get(
					RootConfig.class.getClassLoader().getResource("db_ip.txt").toURI().toString().substring(6)));
			server = lines.get(0);
		} catch (Exception e) {
			server = "localhost";
		}

		String mongoHost = server;
		int mongoPort = 27017;	
		// Mongo Client
		MongoClient mongoClient = new MongoClient(mongoHost, mongoPort);
		// Mongo DB Factory
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongoClient, databaseName);
		return simpleMongoDbFactory;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory());
	}

}
