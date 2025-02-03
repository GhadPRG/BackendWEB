package it.unical.web.backend.service.Response;

import lombok.Getter;

@Getter
public class JWTResponse {
    private final String token;

    public JWTResponse(String token) {
        this.token = token;
    }
}
