package it.unical.web.backend.persistence.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class Meal {
    private int id;
    private User user;
    private String mealType;
    private LocalDate mealDate;
    private List<Dish> dishes;

    // Getters and Setters
}