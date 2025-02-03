package it.unical.web.backend.config;

import it.unical.web.backend.persistence.dao.UserDAOImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> (UserDetails) UserDAOImpl.getByUsername(username);
    }
}