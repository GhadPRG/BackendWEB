package it.unical.web.backend.service;

import it.unical.web.backend.controller.DTO.CalendarDTO;
import it.unical.web.backend.persistence.dao.CalendarEventDAOImpl;
import it.unical.web.backend.persistence.model.CalendarEvent;
import it.unical.web.backend.persistence.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarEventService {

    public void addEvent(CalendarEvent calendarEvent) {
        CalendarEventDAOImpl calendarEventDAO=new CalendarEventDAOImpl();
        calendarEventDAO.addEvent(calendarEvent);
    }

    public List<CalendarDTO> getAllEvents(User userId) {
        CalendarEventDAOImpl calendarEventDAO=new CalendarEventDAOImpl();
        CalendarDTO calendarDTO=new CalendarDTO();
        return calendarDTO.GetCalendarDTOS( calendarEventDAO.getAllEventsByUser(userId));
    }

    public void deleteEvent(int id) {
        CalendarEventDAOImpl calendarEventDAO=new CalendarEventDAOImpl();
        calendarEventDAO.deleteEvent(id);
    }


}
