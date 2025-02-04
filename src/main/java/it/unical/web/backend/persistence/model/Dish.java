package it.unical.web.backend.persistence.model;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Dish {
    private int id;
    private Meal meal;
    private DishInfo dishInfo;
    private int quantity;

    // Getters and Setters
}
