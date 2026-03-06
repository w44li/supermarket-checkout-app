package com.supermarket.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main application class for the Supermarket Checkout App
 * 
 * The @SpringBootApplication annotation is a convenience annotation that combines:
 * - @Configuration: Marks this as a configuration class
 * - @EnableAutoConfiguration: Tells Spring Boot to auto-configure based on classpath
 * - @ComponentScan: Tells Spring to scan for components in this package and sub-packages
 */
@SpringBootApplication
public class CheckoutApplication {

    /**
     * Main method - the entry point of our Spring Boot application
     * 
     * SpringApplication.run() does the following:
     * 1. Creates an ApplicationContext (Spring's container)
     * 2. Starts the embedded Tomcat server (auto-selected port)
     * 3. Auto-configures beans based on our dependencies
     * 4. Scans for components and registers them
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CheckoutApplication.class, args);
        
        // Get the actual port that Spring Boot selected
        ServletWebServerApplicationContext webServerAppCtxt = (ServletWebServerApplicationContext) context;
        int port = webServerAppCtxt.getWebServer().getPort();
        
        System.out.println("🛒 Supermarket Checkout App is running!");
        System.out.println("🌐 Visit: http://localhost:" + port);
        System.out.println("🗄️  Database Console: http://localhost:" + port + "/h2-console");
    }
}