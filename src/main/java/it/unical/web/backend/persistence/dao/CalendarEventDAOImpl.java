package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.CalendarEventDAO;
import it.unical.web.backend.persistence.model.CalendarEvent;
import it.unical.web.backend.persistence.model.Tag;
import it.unical.web.backend.persistence.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarEventDAOImpl implements CalendarEventDAO {

    private final Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public int addEvent(CalendarEvent event) {
        String sql = "INSERT INTO calendar_events (user_id, category_id, title, description, start_event, end_event) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, event.getUserId().getId());
            stmt.setObject(2, event.getCategoryId()); // Può essere null
            stmt.setString(3, event.getTitle());
            stmt.setString(4, event.getDescription());
            stmt.setTimestamp(5, Timestamp.valueOf(event.getStart()));

            stmt.setTimestamp(6, Timestamp.valueOf(event.getEnd()));
            System.out.println("Date: "+event.getStart()+" "+event.getEnd());
            stmt.executeUpdate();

            // Recupera l'ID generato automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getInt(1));
                }
            }

            TagDAOImpl tagDAO = new TagDAOImpl();
            for (Tag tag : event.getTags()) {
                tagDAO.addTagToEntity(event.getId(), "calendar_event", tag.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'inserimento dell'evento", e);
        }
        return event.getId();
    }

    @Override
    public void updateEvent(CalendarEvent event) {
        String sql = "UPDATE calendar_events SET user_id = ?, category_id = ?, title = ?, description = ?, start_event = ?, end_event = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, event.getUserId().getId());
            stmt.setObject(2, event.getCategoryId()); // Può essere null
            stmt.setString(3, event.getTitle());
            stmt.setString(4, event.getDescription());
            stmt.setTimestamp(5, Timestamp.valueOf(event.getStart()));
            stmt.setTimestamp(6, Timestamp.valueOf(event.getEnd()));
            stmt.setInt(7, event.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun evento trovato con ID: " + event.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'aggiornamento dell'evento", e);
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        String sql = "DELETE FROM calendar_events WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun evento trovato con ID: " + eventId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante l'eliminazione dell'evento", e);
        }
    }

    @Override
    public CalendarEvent getEventById(int eventId) {
        String sql = "SELECT * FROM calendar_events WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEvent(rs);
                } else {
                    return null; // Nessun evento trovato
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero dell'evento", e);
        }
    }

    @Override
    public List<CalendarEvent> getAllEventsByUser(User userId) {
        String sql = "SELECT * FROM calendar_events WHERE user_id = ?";
        List<CalendarEvent> events = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    events.add(mapRowToEvent(rs));

                }
            }



        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il recupero degli eventi dell'utente", e);
        }

        return events;
    }



    private CalendarEvent mapRowToEvent(ResultSet rs) throws SQLException {
        CalendarEvent event = new CalendarEvent();

        // Imposta i campi dell'evento
        event.setId(rs.getInt("id"));

        // Crea un oggetto User e imposta l'ID
        User user = new User();
        user.setId(rs.getInt("user_id"));
        event.setUserId(user); // Imposta l'oggetto User completo

        // Imposta la categoria (può essere null)
        event.setCategoryId(rs.getObject("category_id", Integer.class));

        // Imposta titolo e descrizione
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));

        // Converte Timestamp in LocalDateTime per start e end
        event.setStart(rs.getTimestamp("start_event").toLocalDateTime());
        event.setEnd(rs.getTimestamp("end_event").toLocalDateTime());
        TagDAOImpl tagDAO=new TagDAOImpl();
        event.setTags(tagDAO.getTagsByEntityIdAndType(event.getId(), "calendar_event"));

        return event;
    }
}