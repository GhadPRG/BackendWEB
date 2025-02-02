package it.unical.web.backend.persistence.model;
//Gestisce la relazione molti a molti tra workout e workoutplan

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WorkoutPlanAndExercise {
    private long workoutPlanId;
    private long exerciseId;
}
