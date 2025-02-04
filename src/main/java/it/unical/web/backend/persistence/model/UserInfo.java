package it.unical.web.backend.persistence.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserInfo {
    private User user; // Relazione con User
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birth;
    private String gender;
    private float height;
    private float weight;
    private int dailyKcalories;

    public UserInfo(String firstName, String lastName, String email, LocalDate birth, String gender, float height, float weight, int dailyKcalories) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birth = birth;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.dailyKcalories = dailyKcalories;
    }

    // Getters and Setters
}