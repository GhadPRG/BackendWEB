package it.unical.web.backend.persistence.model;
import lombok.*;

import javax.management.ConstructorParameters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Exercise {
    private long id;
    private String name;
    private String notes;
    private String muscleGroup;
    private int reps;
    private int sets;
    private float kcalPerRep;
    private float weight;
    private User createdBy;

    // Getters and Setters
}