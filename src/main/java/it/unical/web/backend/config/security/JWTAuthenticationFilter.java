package it.unical.web.backend.config.security;

import it.unical.web.backend.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    public JWTAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromHeader(request);
        if (token == null) {
            logger.warn("Nessun token trovato, continua la catena di filtri...");
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtService.isTokenExpired(token)) {
            logger.warn("Token scaduto, accesso negato.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token JWT scaduto.");
            return;
        }

        Authentication authentication = jwtService.getAuthentication(token);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Autenticazione avvenuta con successo per utente: " + authentication.getName());
        } else {
            logger.warn("Autenticazione non riuscita.");
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        return jwtService.extractToken(request);
    }
}

