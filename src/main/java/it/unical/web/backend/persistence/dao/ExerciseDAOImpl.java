package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.ExerciseDAO;
import it.unical.web.backend.persistence.model.Exercise;
import it.unical.web.backend.persistence.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class ExerciseDAOImpl implements ExerciseDAO {
    private Connection connection=DatabaseConnection.getConnection();

    @Override
    public Exercise getExerciseById(int id) {
        String query = "SELECT * FROM exercises WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(rs.getInt("id"));
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

                return exercise;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Exercise> getAllExercisesByUser(int userId) {
        List<Exercise> exercises = new ArrayList<>();
        String query = "SELECT * FROM exercises WHERE created_by = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(rs.getInt("id"));
                exercise.setName(rs.getString("name"));
                exercise.setNotes(rs.getString("notes"));
                exercise.setMuscleGroup(rs.getString("muscle_group"));
                exercise.setReps(rs.getInt("reps"));
                exercise.setSets(rs.getInt("sets"));
                exercise.setKcalPerRep(rs.getFloat("kcal_per_rep"));
                exercise.setWeight(rs.getFloat("weight"));

                // Fetch the user who created the exercise
                User createdBy = new User();
                createdBy.setId(userId); // Assume the user is already fetched elsewhere
                exercise.setCreatedBy(createdBy);

                exercises.add(exercise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    @Override
    public void createExercise(Exercise exercise) {
        String query = "INSERT INTO exercises (name, notes, muscle_group, reps, sets, kcal_per_rep, weight, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getNotes());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setInt(4, exercise.getReps());
            stmt.setInt(5, exercise.getSets());
            stmt.setFloat(6, exercise.getKcalPerRep());
            stmt.setFloat(7, exercise.getWeight());
            stmt.setInt(8, exercise.getCreatedBy().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                exercise.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateExercise(Exercise exercise) {
        String query = "UPDATE exercises SET name = ?, notes = ?, muscle_group = ?, reps = ?, sets = ?, kcal_per_rep = ?, weight = ?, created_by = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, exercise.getName());
            stmt.setString(2, exercise.getNotes());
            stmt.setString(3, exercise.getMuscleGroup());
            stmt.setInt(4, exercise.getReps());
            stmt.setInt(5, exercise.getSets());
            stmt.setFloat(6, exercise.getKcalPerRep());
            stmt.setFloat(7, exercise.getWeight());
            stmt.setInt(8, exercise.getCreatedBy().getId());
            stmt.setFloat(9, exercise.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteExercise(int id) {
        String query = "DELETE FROM exercises WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}