package it.unical.web.backend.persistence.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class WorkoutPlan {
    private int id;
    private String name;
    private String description;
    private User createdBy;
    private List<Exercise> exercises;

    // Getters and Setters
}