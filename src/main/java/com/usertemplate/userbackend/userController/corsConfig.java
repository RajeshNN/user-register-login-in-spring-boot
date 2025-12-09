package com.usertemplate.userbackend.userController;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class corsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applies CORS to all paths in the API
                .allowedOrigins("http://localhost:3000", "https://hoppscotch.io", "*") // Specific allowed origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allowed headers in the request
                .allowCredentials(false) // Allow cookies/auth headers to be sent
                .maxAge(3600); // How long the preflight response can be cached (in seconds)
    }
}
