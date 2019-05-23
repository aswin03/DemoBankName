package com.edubank.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * This class is a configuration class for Dispatcher Servlet.  
 * 
 * This class extends {@link WebMvcConfigurerAdapter} abstract class. <br>
 * 
 * It overrides <b>addResourceHandlers()</b> method of {@link WebMvcConfigurerAdapter} abstract class
 * to add handlers to serve static resources such as images, css and any other resource files from specific locations
 * under web application root (i.e., Webcontent folder). <br>
 * 
 * 
 * @Configuration
 * This will let spring know that this class contains one or more @Bean methods (methods using which object creation is 
 * made automatic). "viewResolver()" method is made as  @Bean method in this class. 
 * 
 * @EnableWebMvc
 * Adding this annotation to an @Configuration class imports the Spring MVC configuration from WebMvcConfigurationSupport.
 * By adding this the 2 overridden methods in this class get invoked automatically.
 * 
 * @ComponentScan
 * It is to scan the classes in the packages com.edubank.controller and com.edubank.api. These 2 packages are given
 * as base packages to this annotation.
 * 
 * @author ETA_JAVA
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.edubank.controller com.edubank.api" )
public class DispatcherConfig extends WebMvcConfigurerAdapter
{
	
    /**
     * This method is to create an object for <b>InternalResourceViewResolver</b> 
     * (it supports internal resource view i.e., servlets and JSPs and it is for final output, jsp or htmp page.
     * It is used to resolve the provided URI from client to actual URI where the resources which have to sent to client are
     * present)   class and to populated the created object by
     * setting the path of the views (JSP pages) in the project and to set the type of file which gives the view. 
     * Here it is jsp file.  <br>
     * 
     * The created object becomes a bean class for spring container to manage as this method is annotated with @Bean annotation.
     * 
     * @return returns created view resolver object. 
     */
    @Bean
    public ViewResolver viewResolver() 
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        
        /*
         * The below statement is to set the view class that should be used to create views.
         */
        viewResolver.setViewClass(JstlView.class);
        
        /*
         * The below statement is to set the prefix that gets prepended to view names.
         * Here it is "/WEB-INF/views/" because JSP files (views) are present in that path.
         */
        viewResolver.setPrefix("/WEB-INF/views/");
        
        /*
         * The below statement is to set the suffix that gets appended to view names.
         * Here the suffix is "jsp" as all the views are of JSP files .
         */
        viewResolver.setSuffix(".jsp");
        
        return viewResolver;
    }
    
    /**
     * This is an overridden method of {@link WebMvcConfigurerAdapter} abstract class. <br>
     * 
     *  This method is to add resource handlers to serve static resources such as images, css and any other resource files
     *  from specific locations under web application root (i.e., Webcontent folder). 
     *  
     *  Here the resources location is given as "/WEB-INF/resources/"
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) 
    {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
    }
}
