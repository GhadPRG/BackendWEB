package it.unical.web.backend.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permette tutte le rotte
                        .allowedOrigins("http://localhost:4200", "http://localhost:4200/") // Permette il frontend Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permette i metodi richiesti
                        .allowedHeaders("*") // Permette tutti gli header
                        .allowCredentials(true); // Permette i cookie, se necessari
            }
        };
    }
}