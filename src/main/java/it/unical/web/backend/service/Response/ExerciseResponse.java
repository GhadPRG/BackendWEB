package it.unical.web.backend.service.Response;

import it.unical.web.backend.persistence.model.Exercise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseResponse {
    private long id;
    private String name;
    private String notes;
    private String muscleGroup;
    private int reps;
    private int sets;
    private float met;
    private float weight;

    public ExerciseResponse(Exercise exercise) {
        this.id = exercise.getId();
        this.name = exercise.getName();
        this.notes = exercise.getNotes();
        this.muscleGroup = exercise.getMuscleGroup();
        this.reps = exercise.getReps();
        this.sets = exercise.getSets();
        this.met = exercise.getMet();
        this.weight = exercise.getWeight();
    }
}
