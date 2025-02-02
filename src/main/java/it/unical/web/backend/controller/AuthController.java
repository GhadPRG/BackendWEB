package it.unical.web.backend.controller;

import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.service.AuthService;
import it.unical.web.backend.service.Request.AuthenticationRequest;
import it.unical.web.backend.service.Request.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AuthController {

    AuthService authService;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(RegistrationRequest registrationRequest) {
        return authService.registerUser(registrationRequest);
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authRequest) {
        return authService.login(authRequest);
    }
}
