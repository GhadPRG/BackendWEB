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
}