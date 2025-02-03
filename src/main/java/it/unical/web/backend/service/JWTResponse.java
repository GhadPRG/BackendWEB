package it.unical.web.backend.service;

import lombok.Getter;
import lombok.Setter;

@Getter
public class JWTResponse {
    private final String token;

    public JWTResponse(String token) {
        this.token = token;
    }
}
