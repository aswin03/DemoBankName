package com.edubank.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This is a configuration class for creating bean objects which are managed by spring bean container (a container of objects
 * which are called as beans in spring framework). <br>
 * 
 * It creates SessionFactory bean object, DataSource bean object (used to create SessionFactory object) and
 * TransactionManager object (created by using SessionFactory object and DataSource object). <br>
 * 
 * @Configuration
 * This will let spring know that this class contains one or more @Bean methods (methods using which object creation is 
 * made automatic).
 * 
 * @EnableAspectJAutoProxy
 * Enables support for having Spring AOP (Aspect Oriented Programming, using which logging, transaction management, etc.
 * logics are made available at only one place in project instead of having in different places. These are called
 * cross-cutting concerns. It eliminates the dependency of having these cross-cutting concerns at different places in the 
 * project) feature in the application.
 * 
 * @EnableTransactionManagement
 * Enables Spring's annotation-driven transaction management capability for the project.
 * 
 * @PropertySource
 * This is to add a property source to Spring's Environment. Here the property source is "database.properties"
 * which has Database related information and "configuration.properties" having message key and value pairs.
 * 
 * @ComponentScan
 * It is to scan the classes in the package "com.edubank". This package is given as base package to this annotation.
 * It scans DAO, Service and Utility packages.
 *  
 * @author ETA_JAVA
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:/com/edubank/resources/database.properties"),
	              @PropertySource("classpath:/com/edubank/resources/configuration.properties")})
@ComponentScan(basePackages = "com.edubank.dao com.edubank.service com.edubank.utility")
public class SpringConfig
{
    
	/**
	 * The attribute is used for getting property values from <b>database.properties</b> file.
	 * It is annotated with "@Autowired" Spring annotation which makes it get instantiated and initialized by
	 * spring framework automatically.
	 */
	@Autowired
    private Environment environment;
    
	
	/**
	 * This method is to create and populate DataSource object with database driver class, database url,
	 * database user name and password.
	 * 
	 * This dataSource object is used to create sessionFactory object.
	 * 
	 * The created object becomes a bean class for spring container to manage as this method 
	 * is annotated with @Bean annotation.
     * 
	 * @return data source object
	 */
	@Bean
    public DataSource dataSource() 
	{
		/*
		 * The below code is to create and populate DataSource object with database driver class, database url,
		 * database user name and password by getting these values using environment variable. <br>
		 * 
		 */
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        return dataSource;
    }

    
	/**
	 * This method is to create and populate SessionFactory object with hibernate properties.
	 * This SessionFactory object gets auto-wired in all the DAO classes automatically by spring framework
	 * using which database session are created. 
	 * 
	 * The created object becomes a bean class for spring container to manage as this method 
	 * is annotated with @Bean annotation.
     * 
	 * @return sessionFactory object
	 */
    @Bean("sessionFactory")
    public LocalSessionFactoryBean getSessionFactory(DataSource dataSource)
    {
    	
     	
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        
        /*
         * The received  dataSource object which has database information is set to sessionFactoryBean
         */
        sessionFactoryBean.setDataSource(dataSource);
        
        /*
         * The below code is to set the package in which Entity classes (the classes to which Database tables are mapped)
         * are present to sessionFactoryBean
         */
        sessionFactoryBean.setPackagesToScan("com.edubank.entity");
        
        /*
         * The below code is to create and populate Properties object with Hibernate properties.
         * Hibernate property values are taken from database.properties file using environment variable.
         * 
         */
        Properties hibernateProperties = new Properties();
       
        hibernateProperties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
        
        /*
         * In the below statement hibernateProperties object created above is set to sessionFactoryBean
         */
        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        
        return sessionFactoryBean;
    }

    
    /**
	 * This method is to create and populate TransactionManager object with sessionFactory and dataSource.
	 * This transactionManager object is to used in  Spring's annotation-driven transaction management.
	 * 
	 * The created object becomes a bean class for spring container to manage as this method 
	 * is annotated with @Bean annotation.
     * 
	 * @return transactionManager object
	 */
    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory,DataSource dataSource)
    {
    	 /*
    	  * In the below code transactionManager object is created and the received sessionFactory and dataSource
    	  * objects are set to transactionManager object.
    	  */
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
    
}
