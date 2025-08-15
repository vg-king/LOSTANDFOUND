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
                        "http://localhost:*",        // All localhost ports
                        "http://127.0.0.1:*",       // All 127.0.0.1 ports
                        // Production origins
                        "https://kiitfinderui-abbi.vercel.app",
                        "https://lostandfound-1-p1l9.onrender.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")  // Allow all headers - simpler approach
                .exposedHeaders(
                        "Authorization",
                        "Content-Type",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }
}

// Alternative - More explicit version if the above doesn't work
/* 
@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        // Local development
                        "http://localhost:3000",
                        "http://localhost:3001", 
                        "http://localhost:8080",
                        "http://localhost:5173",
                        "http://127.0.0.1:3000",
                        "http://127.0.0.1:8080",
                        // Production
                        "https://kiitfinderui-abbi.vercel.app",
                        "https://lostandfound-1-p1l9.onrender.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
*/