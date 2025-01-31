package it.unical.web.backend.Persistence.Model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Date birthDate;
    private String gender;
    private float height;
    private float weight;
    private int dailyCalories;
}
