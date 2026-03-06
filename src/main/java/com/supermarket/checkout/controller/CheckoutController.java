package com.supermarket.checkout.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for handling checkout-related HTTP requests
 * 
 * @RestController combines @Controller and @ResponseBody:
 * - @Controller: Marks this as a Spring MVC controller
 * - @ResponseBody: Automatically converts return values to JSON (or XML)
 * 
 * @RequestMapping: Base URL mapping for all endpoints in this controller
 */
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    /**
     * Simple welcome endpoint
     * 
     * @GetMapping: Handles HTTP GET requests to /api/checkout/welcome
     * Spring automatically converts the Map to JSON response
     */
    @GetMapping("/welcome")
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Supermarket Checkout System!");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "running");
        return response;
    }

    /**
     * Endpoint with path variable
     * 
     * @PathVariable: Extracts the {customerName} from the URL path
     * Example: GET /api/checkout/hello/John -> returns greeting for John
     */
    @GetMapping("/hello/{customerName}")
    public Map<String, String> greetCustomer(@PathVariable String customerName) {
        Map<String, String> response = new HashMap<>();
        response.put("greeting", "Hello, " + customerName + "!");
        response.put("message", "Ready to process your checkout?");
        return response;
    }

    /**
     * Health check endpoint
     * 
     * This is useful for monitoring if your application is running properly
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Checkout Service");
        return response;
    }
}