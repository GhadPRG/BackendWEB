package it.unical.web.backend.controller;

import it.unical.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/auth/user")
    public ResponseEntity<?> getUser() {
        return userService.getUserInfo();
    }
}
