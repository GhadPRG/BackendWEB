package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.NoteDAO;
import it.unical.web.backend.persistence.model.Category;
import it.unical.web.backend.persistence.model.Note;
import it.unical.web.backend.persistence.model.Tag;
import it.unical.web.backend.persistence.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAOImpl implements NoteDAO {
    private final Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public Note getNoteById(long id) {
        System.out.println("getNoteById, cerco note per utente"+id);
        String query = "SELECT * FROM notes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setCreatedAt(rs.getDate("created_at").toLocalDate());

                System.out.println("Sono dentro if");
                // Fetch the user who created the note
                User user = new User();
                user.setId(rs.getInt("user_id")); // Assume the user is already fetched elsewhere
                note.setUser(user);

                // Fetch tags associated with this note
                note.setTags(getTagsByEntityIdAndType(id, "note"));

                return note;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Restituisci null se nessuna nota viene trovata
    }

    @Override
    public List<Note> getAllNotesByUser(int userId) {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM notes WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note note = new Note();
                note.setId(rs.getInt("id"));
                note.setTitle(rs.getString("title"));
                note.setContent(rs.getString("content"));
                note.setCreatedAt(rs.getDate("created_at").toLocalDate());

                // Fetch the user who created the note
                User user = new User();
                user.setId(userId); // Assume the user is already fetched elsewhere
                note.setUser(user);

                // Fetch tags associated with this note
                note.setTags(getTagsByEntityIdAndType(note.getId(), "note"));


                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }

    @Override
    public Note createNote(Note note) {
        String query = "INSERT INTO notes (user_id, title, content, created_at) VALUES (?, ?, ?, CURRENT_DATE)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, note.getUser().getId());
            stmt.setString(2, note.getTitle());
            stmt.setString(3, note.getContent());
            //stmt.setDate(4, Date.valueOf(note.getCreatedAt()));
            stmt.executeUpdate();

            // Recupera l'ID generato automaticamente
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                note.setId(rs.getInt(1)); // Assegna l'ID alla nota
            } else {
                System.err.println("Errore: Nessun ID generato per la nota.");
            }

            System.out.println("Tag"+note.getTags());
            //Aggiungi i tag alla nota
            TagDAOImpl tagDAO=new TagDAOImpl();
            for (Tag tag : note.getTags()) {
                tagDAO.addTagToEntity(note.getId(), "note", tag.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getNoteById(note.getId());
    }

    @Override
    public void updateNote(Note note) {
        String query = "UPDATE notes SET user_id = ?, title = ?, content = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, note.getUser().getId());
            stmt.setString(2, note.getTitle());
            stmt.setString(3, note.getContent());
            //stmt.setDate(4, Date.valueOf(note.getCreatedAt()));
            stmt.setInt(4, note.getId());
            stmt.executeUpdate();

            // Update tags for the note
            removeTagsFromEntity(note.getId(), "note");
            TagDAOImpl tagDAO=new TagDAOImpl();
            for (Tag tag : note.getTags()) {
                tagDAO.addTagToEntity(note.getId(), "note", tag.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNote(int id) {
        String query = "DELETE FROM notes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Remove tags associated with the note
            removeTagsFromEntity(id, "note");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Tag> getTagsByEntityIdAndType(long entityId, String entityType) {
        List<Tag> tags = new ArrayList<>();
        String query = "SELECT t.* FROM tags t " +
                "JOIN entity_tags et ON t.id = et.tag_id " +
                "WHERE et.entity_id = ? AND et.entity_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, entityId);
            stmt.setString(2, entityType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setDescription(rs.getString("description"));

                // Fetch the category of the tag
                Category category = new Category();
                category.setId(rs.getInt("category_id")); // Assume the category is already fetched elsewhere
                tag.setCategory(category);

                tags.add(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    private void removeTagsFromEntity(int entityId, String entityType) {
        String query = "DELETE FROM entity_tags WHERE entity_id = ? AND entity_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entityId);
            stmt.setString(2, entityType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}