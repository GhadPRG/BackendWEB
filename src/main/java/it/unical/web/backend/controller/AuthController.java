package it.unical.web.backend.controller;

import it.unical.web.backend.persistence.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AuthController {

    @PostMapping("/api/register")
    public ResponseEntity register(@RequestBody User user) {

    }


}
