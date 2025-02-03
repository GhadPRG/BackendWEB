package it.unical.web.backend.persistence.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    private long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private float height;
    private float weight;
    private int dailyCalories;
}
