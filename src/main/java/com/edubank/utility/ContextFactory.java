package com.edubank.utility;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.edubank.configuration.SpringConfig;


/**
 * This Context Factory class is used to create object of applicationContext
 * which creates spring container.
 * 
 * @author ETA_JAVA
 *
 */
public class ContextFactory {

	 /**
	  * Here we are parsing the SpringConfig class in which 
	  * configuration is written that object of which all classes 
	  * has to be created and kept in spring container
	  * 
	  * (Please see {@link SpringConfig} class for more details)
	  * 
	  * ApplicationContext(spring container) holds the objects created by spring framework
	  */
	private static final ApplicationContext CONTEXT = new AnnotationConfigApplicationContext(SpringConfig.class);
	
	/**
	 * getContext() method of ContextFactory class parses the
	 * SpringConfig class and using @ComponentScan,
	 * 
	 * it scans the packages/classes for which the base package provided
	 * is 'com.edubank'
	 * then it returns the object of ApplicationContext
	 * 
	 * ApplicationContext holds the objects created by spring framework
	 */
	public static ApplicationContext getContext() {
		return CONTEXT ;
	}
}
