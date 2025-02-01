package it.unical.web.backend.service.Request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {
    String username, firstname, lastname, password, email, gender, birthDate;
    float height, weight;
    int dailyCalories;
}