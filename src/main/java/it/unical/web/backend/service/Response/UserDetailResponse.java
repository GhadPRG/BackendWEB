package it.unical.web.backend.service.Response;

import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.UserInfo;
import lombok.Getter;
import java.time.LocalDate;


@Getter
public class UserDetailResponse {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private float height;
    private float weight;
    private int dailyCalories;

    public UserDetailResponse(UserInfo user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.birthDate = user.getBirth();
        this.gender = user.getGender();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.dailyCalories = user.getDailyKcalories();
    }
}
