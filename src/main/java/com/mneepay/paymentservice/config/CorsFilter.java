package com.mneepay.paymentservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsFilter implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // apply to all endpoints
                .allowedOrigins(
                        "https://rapydmnee.netlify.app/" // Replace with your Netlify URL
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // allow standard methods
                .allowedHeaders("*")   // allow all headers
                .allowCredentials(true) // allow cookies/auth
                .maxAge(3600);          // cache preflight response for 1 hour
    }
}
