package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.CalendarEvent;
import it.unical.web.backend.persistence.model.User;

import java.util.List;

public interface CalendarEventDAO {
    int addEvent(CalendarEvent event);
    void updateEvent(CalendarEvent event);
    void deleteEvent(int eventId);
    CalendarEvent getEventById(int eventId);
    List<CalendarEvent> getAllEventsByUser(User userId);
}