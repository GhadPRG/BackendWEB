package it.unical.web.backend.persistence.model;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DishInfo {
    private int id;
    private String nome;
    private int kcalories;
    private int carbs;
    private int proteins;
    private int fats;
    private int fibers;
    private User createdBy;

    // Getters and Setters
}
