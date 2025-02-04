package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.WorkoutPlan;

import java.util.List;

public interface WorkoutPlanDAO {
    WorkoutPlan getWorkoutPlanById(int id);
    List<WorkoutPlan> getAllWorkoutPlansByUser(int userId);
    void createWorkoutPlan(WorkoutPlan workoutPlan);
    void updateWorkoutPlan(WorkoutPlan workoutPlan);
    void deleteWorkoutPlan(int id);

    void addExerciseToWorkoutPlan(long workoutPlanId, long exerciseId);

    void removeExerciseFromWorkoutPlan(long workoutPlanId, long exerciseId);
}
