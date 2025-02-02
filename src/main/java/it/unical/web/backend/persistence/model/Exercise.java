package it.unical.web.backend.persistence.model;
import lombok.*;

import javax.management.ConstructorParameters;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Exercise {
    private Long id;
    private String name;
    private String notes;
    private String muscleGroup;
    private int reps;
    private int sets;
    private double kcalPerRep;
    private Double weight; // Nullable
    private Long createdBy;

    public Exercise(String name, String notes, String muscleGroup, int reps, int sets, double kcalPerRep, Double weight, Long createdBy) {
        this.name = name;
        this.notes = notes;
        this.muscleGroup = muscleGroup;
        this.reps = reps;
        this.sets = sets;
        this.kcalPerRep = kcalPerRep;
        this.weight = weight;
        this.createdBy = createdBy;
    }
}
