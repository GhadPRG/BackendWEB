package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.BaseDAO;
import it.unical.web.backend.persistence.model.Mood;
import it.unical.web.backend.persistence.model.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MoodDAO implements BaseDAO<Mood> {

    @Override
    public Mood findById(int id) {
        String query = "SELECT * FROM mood_tracker WHERE id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
                PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Mood(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("mood_level"),
                        rs.getDate("mood_date").toLocalDate(),
                        rs.getString("notes"),
                        rs.getInt("note_id") == 0 ? null : rs.getInt("note_id") // Gestione di note_id NULL
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Mood> findAll(long id) {
        List<Mood> moods = new ArrayList<>();
        String query = "SELECT * FROM mood_tracker WHERE user_id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
             PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                moods.add(new Mood(
                        rs.getInt("mood_id"),
                        rs.getInt("user_id"),
                        rs.getInt("mood_level"),
                        rs.getDate("mood_date").toLocalDate(),
                        rs.getString("notes"),
                        rs.getInt("note_id") == 0 ? null : rs.getInt("note_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moods;
    }


    @Override
    public boolean save(Mood mood) {
        String query = "INSERT INTO mood_tracker (user_id, mood_level, mood_date, notes, note_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection dbConnection= DatabaseConnection.getConnection();
             PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setLong(1, mood.getUser());
            stmt.setInt(2, mood.getMoodLevel());
            stmt.setDate(3, java.sql.Date.valueOf(mood.getMoodDate()));
            stmt.setString(4, mood.getNotes());
            stmt.setObject(6, mood.getNoteId(), Types.INTEGER); // Supporta NULL
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Mood mood) {
        String query = "UPDATE mood_tracker SET mood_level = ?, mood_date = ?, notes = ?, note_id = ? WHERE id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
             PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, mood.getMoodLevel());
            stmt.setDate(2, java.sql.Date.valueOf(mood.getMoodDate()));
            stmt.setString(3, mood.getNotes());
            stmt.setObject(4, mood.getNoteId(), Types.INTEGER); // Supporta NULL
            stmt.setLong(5, mood.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM mood_tracker WHERE id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
             PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Note> findNotesWithMood() {
        List<Note> notes = new ArrayList<>();
        String query = """
        SELECT n.*, m.mood_level, m.mood_date
        FROM notes n
        LEFT JOIN mood_tracker m ON n.id = m.note_id
    """;
        try (Connection dbConnection= DatabaseConnection.getConnection();
             PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Note note = new Note(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_at").toLocalDate()
                );
                //TODO: Fare in modo che il metodo restituisca anche l'umore per ogni nota, o i tag aggiunti.
                // Da verificare poi in fase di implementazione vera e propria.
                if (rs.getInt("mood_level") != 0) {
                    System.out.println("Umore associato: " + rs.getInt("mood_level"));
                }
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }
}