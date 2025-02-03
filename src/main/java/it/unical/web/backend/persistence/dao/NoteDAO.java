package it.unical.web.backend.persistence.dao;
import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.BaseDAO;
import it.unical.web.backend.persistence.model.Note;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO implements BaseDAO<Note> {


    @Override
    public Note findById(int id) {
        String query = "SELECT * FROM \"notes\" WHERE id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
                PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Note(
                        rs.getInt("id"),
                        rs.getLong("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_at").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.out.println("Eccezione in findById(NoteDAO): " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Note> findAll(long id) {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM notes WHERE user_id=?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
                PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("created_at").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Eccezione in findAll(NoteDAO): " + e.getMessage());
        }
        return notes;
    }

    @Override
    public boolean save(Note note) {
        String query = "INSERT INTO notes (user_id, title, content, created_at) VALUES (?, ?, ?, ?)";
        try (Connection dbConnection= DatabaseConnection.getConnection();
                PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setLong(1, note.getUser());
            stmt.setString(2, note.getTitle());
            stmt.setString(3, note.getContent());
            stmt.setDate(4, java.sql.Date.valueOf(note.getCreated_at()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Eccezione in save(NoteDAO): " + e.getMessage());

        }
        return false;
    }

    @Override
    public boolean update(Note note) {
        String query = "UPDATE notes SET title = ?, content = ? WHERE id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
                PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, note.getTitle());
            stmt.setString(2, note.getContent());
            stmt.setLong(4, note.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Eccezione in update(NoteDAO): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "DELETE FROM notes WHERE id = ?";
        try (Connection dbConnection= DatabaseConnection.getConnection();
                PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Eccezione in delete(NoteDAO): " + e.getMessage());

        }
        return false;
    }
}