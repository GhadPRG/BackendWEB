package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.WorkoutPlanDAO;
import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.WorkoutPlan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class WorkoutPlanDAOImpl implements WorkoutPlanDAO {
    private Connection connection = DatabaseConnection.getConnection();

    @Override
    public WorkoutPlan getWorkoutPlanById(int id) {
        String query = "SELECT * FROM workout_plans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                WorkoutPlan workoutPlan = new WorkoutPlan();
                workoutPlan.setId(rs.getInt("id"));
                workoutPlan.setName(rs.getString("name"));
                workoutPlan.setDescription(rs.getString("description"));

                // Fetch the user who created the workout plan
                User createdBy = new User();
                createdBy.setId(rs.getInt("created_by")); // Assume the user is already fetched elsewhere
                workoutPlan.setCreatedBy(createdBy);

                // Fetch exercises associated with this workout plan
                workoutPlan.setExercises(getExercisesByWorkoutPlanId(id));
                return workoutPlan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<WorkoutPlan> getAllWorkoutPlansByUser(int userId) {
        List<WorkoutPlan> workoutPlans = new ArrayList<>();
        String query = "SELECT * FROM workout_plans WHERE created_by = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                WorkoutPlan workoutPlan = new WorkoutPlan();
                workoutPlan.setId(rs.getInt("id"));
                workoutPlan.setName(rs.getString("name"));
                workoutPlan.setDescription(rs.getString("description"));

                // Fetch the user who created the workout plan
                User createdBy = new User();
                createdBy.setId(userId); // Assume the user is already fetched elsewhere
                workoutPlan.setCreatedBy(createdBy);

                // Fetch exercises associated with this workout plan
                workoutPlan.setExercises(getExercisesByWorkoutPlanId(workoutPlan.getId()));
                workoutPlans.add(workoutPlan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workoutPlans;
    }

    @Override
    public void createWorkoutPlan(WorkoutPlan workoutPlan) {
        String query = "INSERT INTO workout_plans (name, description, created_by) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, workoutPlan.getName());
            stmt.setString(2, workoutPlan.getDescription());
            stmt.setInt(3, workoutPlan.getCreatedBy().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                workoutPlan.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWorkoutPlan(WorkoutPlan workoutPlan) {
        // Update the main workout plan data
        String query = "UPDATE workout_plans SET name = ?, description = ?, created_by = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, workoutPlan.getName());
            stmt.setString(2, workoutPlan.getDescription());
            stmt.setInt(3, workoutPlan.getCreatedBy().getId());
            stmt.setInt(4, workoutPlan.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sync the associated exercises
        syncExercisesForWorkoutPlan(workoutPlan);
    }

    @Override
    public void deleteWorkoutPlan(int id) {
        // Delete the workout plan itself
        String deleteWorkoutPlanQuery = "DELETE FROM workout_plans WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteWorkoutPlanQuery)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addExerciseToWorkoutPlan(long workoutPlanId, long exerciseId) {
        String query = "INSERT INTO workout_plan_exercises (workout_plan_id, exercise_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, workoutPlanId);
            stmt.setLong(2, exerciseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeExerciseFromWorkoutPlan(long workoutPlanId, long exerciseId) {
        String query = "DELETE FROM workout_plan_exercises WHERE workout_plan_id = ? AND exercise_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, workoutPlanId);
            stmt.setLong(2, exerciseId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    private void syncExercisesForWorkoutPlan(WorkoutPlan workoutPlan) {
//        int workoutPlanId = workoutPlan.getId();
//        List<Exercise> currentExercises = getExercisesByWorkoutPlanId(workoutPlanId);
//
//        // Get IDs of currently associated exercises
//        Set<Long> currentExerciseIds = currentExercises.stream()
//                .map(Exercise::getId)
//                .collect(Collectors.toSet());
//
//        // Get IDs of exercises to associate
//        Set<Long> newExerciseIds = workoutPlan.getExercises().stream()
//                .map(Exercise::getId)
//                .collect(Collectors.toSet());
//
//        // Find differences
//        Set<Long> exercisesToRemove = new HashSet<>(currentExerciseIds);
//        exercisesToRemove.removeAll(newExerciseIds);
//
//        Set<Long> exercisesToAdd = new HashSet<>(newExerciseIds);
//        exercisesToAdd.removeAll(currentExerciseIds);
//
//        // Remove obsolete associations
//        for (Long exerciseId : exercisesToRemove) {
//            removeExerciseFromWorkoutPlan(workoutPlanId, exerciseId);
//        }
//
//        // Add new associations
//        for (Long exerciseId : exercisesToAdd) {
//            addExerciseToWorkoutPlan(workoutPlanId, exerciseId);
//        }
//    }


    private List<Exercise> getExercisesByWorkoutPlanId(int workoutPlanId) {
        List<Exercise> exercises = new ArrayList<>();
        String query = "SELECT e.* FROM exercises e " +
                "JOIN workout_plan_exercises wpe ON e.id = wpe.exercise_id " +
                "WHERE wpe.workout_plan_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, workoutPlanId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(rs.getLong("id"));
                exercise.setName(rs.getString("name"));
                exercise.setNotes(rs.getString("notes"));
                exercise.setMuscleGroup(rs.getString("muscle_group"));
                exercise.setReps(rs.getInt("reps"));
                exercise.setSets(rs.getInt("sets"));
                exercise.setKcalPerRep(rs.getFloat("kcal_per_rep"));
                exercise.setWeight(rs.getFloat("weight"));

                // Fetch the user who created the exercise
                User createdBy = new User();
                createdBy.setId(rs.getInt("created_by")); // Assume the user is already fetched elsewhere
                exercise.setCreatedBy(createdBy);

                exercises.add(exercise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }
}