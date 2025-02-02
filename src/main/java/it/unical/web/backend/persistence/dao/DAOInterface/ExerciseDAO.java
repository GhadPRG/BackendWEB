package it.unical.web.backend.persistence.dao.DAOInterface;

//TODO: Aggiungere eliminazione di un elemento

import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.WorkoutPlan;
import it.unical.web.backend.persistence.model.WorkoutPlanAndExercise;

import java.util.List;

public interface ExerciseDAO{
    void createExercise(Exercise exercise);
    List<Exercise> getExercisesByUserId(Long id);
    void createWorkoutPlan(WorkoutPlan workoutPlan);
    void addExerciseToWorkoutPlan(WorkoutPlanAndExercise wpaExercise);
    List<Exercise> getExercisesForWorkoutPlan(long workoutPlanId);

}
