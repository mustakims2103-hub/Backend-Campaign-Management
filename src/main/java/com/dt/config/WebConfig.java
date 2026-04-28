//package com.dt.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "Authorization", allowCredentials = "true")
//public class WebConfig implements WebMvcConfigurer {
//
//	
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Allow all endpoints
//                .allowedOrigins("http://localhost:5173") // Replace with your React app URL
//                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
//                .allowedHeaders("*") // Allow all headers
//                .exposedHeaders("Authorization")
//                .allowCredentials(true); // Allow cookies and other credentials
//    }
//}