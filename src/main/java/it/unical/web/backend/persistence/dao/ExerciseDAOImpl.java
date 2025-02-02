package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.ExerciseDAO;
import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.WorkoutPlan;
import it.unical.web.backend.persistence.model.WorkoutPlanAndExercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAOImpl implements ExerciseDAO{

    private Exercise mapToExercise(ResultSet rs) throws SQLException {
        Exercise exercise = new Exercise();
        exercise.setId(rs.getLong("id"));
        exercise.setName(rs.getString("name"));
        exercise.setNotes(rs.getString("notes"));
        exercise.setMuscleGroup(rs.getString("muscle_group"));
        exercise.setReps(rs.getInt("reps"));
        exercise.setSets(rs.getInt("sets"));
        exercise.setKcalPerRep(rs.getDouble("kcal_per_rep"));
        exercise.setWeight(rs.getDouble("weight"));
        exercise.setCreatedBy(rs.getLong("created_by"));
        return exercise;
    }


    public void createExercise(Exercise exercise) {
        String query = "INSERT INTO exercises (name, notes, muscle_group, reps, sets, kcal_per_rep, weight, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try(Connection dbConnection= DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query)){
            preparedStatement.setString(1, exercise.getName());
            preparedStatement.setString(2, exercise.getNotes());
            preparedStatement.setString(3, exercise.getMuscleGroup());
            preparedStatement.setInt(4, exercise.getReps());
            preparedStatement.setInt(5, exercise.getSets());
            preparedStatement.setDouble(6, exercise.getKcalPerRep());
            preparedStatement.setDouble(7, exercise.getWeight());
            preparedStatement.setLong(8, exercise.getCreatedBy());

            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Eccezione in createExercise(ExerciseDAO): " + e.getMessage());
        }


    }

    @Override
    public List<Exercise> getExercisesByUserId(Long userId) {
        String query = "SELECT * FROM exercises WHERE created_by = ?";
        List<Exercise> exercises = new ArrayList<>();
        try (Connection dbConnection=DatabaseConnection.getConnection();
        PreparedStatement preparedStatement=dbConnection.prepareStatement(query);
        ){
            preparedStatement.setLong(1, userId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                exercises.add(mapToExercise(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Eccezione in getExercisesByUserId(ExerciseDAO)"+e.getMessage());
        }

        System.out.println("Trovati: "+exercises.size()+" esercizi");
        return exercises;
    }

    @Override
    public void createWorkoutPlan(WorkoutPlan workoutPlan) {
        String query = "INSERT INTO workout_plans (name, description, created_by) VALUES (?, ?, ?) RETURNING id";

        try(Connection dbConnection=DatabaseConnection.getConnection();
        PreparedStatement preparedStatement=dbConnection.prepareStatement(query)){
            preparedStatement.setString(1,workoutPlan.getName());
            preparedStatement.setString(2, workoutPlan.getDescription());
            preparedStatement.setLong(3, workoutPlan.getCreated_by());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void addExerciseToWorkoutPlan(WorkoutPlanAndExercise wpaExercise) {
        String query = "INSERT INTO workout_plan_exercises (workout_plan_id, exercise_id) VALUES (?, ?)";

        try (Connection dbConnection=DatabaseConnection.getConnection();
             PreparedStatement preparedStatement=dbConnection.prepareStatement(query)){
            preparedStatement.setLong(1, wpaExercise.getWorkoutPlanId());
            preparedStatement.setLong(2, wpaExercise.getExerciseId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Exercise> getExercisesForWorkoutPlan(long workoutPlanId) {
        String query = "SELECT e.* FROM exercises e " +
                "JOIN workout_plan_exercises wpe ON e.id = wpe.exercise_id " +
                "WHERE wpe.workout_plan_id = ?";
        List<Exercise> exercises = new ArrayList<>();

        try (Connection dbConnection= DatabaseConnection.getConnection();
            PreparedStatement preparedStatement=dbConnection.prepareStatement(query)
        ){
            preparedStatement.setLong(1, workoutPlanId);

            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                exercises.add(mapToExercise(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return exercises;
    }
}
