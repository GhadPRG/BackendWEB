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

    public User(String username, String password, String firstName, String lastName, String email, LocalDate birthDate, String gender, float height, float weight, int dailyCalories) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.dailyCalories = dailyCalories;
    }
}
