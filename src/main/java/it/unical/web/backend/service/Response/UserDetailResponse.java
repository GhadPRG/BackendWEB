package it.unical.web.backend.service.Response;

import it.unical.web.backend.persistence.model.User;
import lombok.Getter;
import java.time.LocalDate;


@Getter
public class UserDetailResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private float height;
    private float weight;
    private int dailyCalories;

    public UserDetailResponse(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.dailyCalories = user.getDailyCalories();
    }
}
