package it.unical.web.backend.persistence.model;

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
