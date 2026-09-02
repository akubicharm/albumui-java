package com.example.albumui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main application class for the Album UI application.
 * This class serves as the entry point for the Spring Boot application.
 * 
 * The application is configured to run as a Spring Boot application and can be deployed as a WAR file.
 * It extends SpringBootServletInitializer to support traditional deployment in a servlet container.	
 */
@SpringBootApplication
public class App extends SpringBootServletInitializer{

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
    {
        return application.sources(App.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
