package it.unical.web.backend.controller;

import it.unical.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/api/auth/user")
    public ResponseEntity<?> getUser() {
        return userService.getUserInfo();
    }
}
