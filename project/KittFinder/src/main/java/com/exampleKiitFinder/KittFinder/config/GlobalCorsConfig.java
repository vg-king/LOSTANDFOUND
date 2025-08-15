package com.exampleKiitFinder.KittFinder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        // Local development origins
                        "http://localhost:3000",
                        "http://localhost:3001",
                        "http://localhost:8080",    // Add this - your current frontend
                        "http://localhost:5173",    // Vite default
                        "http://127.0.0.1:3000",
                        "http://127.0.0.1:8080",    // Add this too
                        // Production origins
                        "https://kiitfinderui-abbi.vercel.app",
                        "https://*.vercel.app",
                        // Your deployed backend (if frontend calls it)
                        "https://lostandfound-1-p1l9.onrender.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
                .exposedHeaders(
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials",
                        "Authorization"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}