package it.unical.web.backend.service.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseRequest {
    private String name;
    private String notes;
    private String muscleGroup;
    private int reps;
    private int sets;
    private float kcalPerRep;
    private float weight;
}
