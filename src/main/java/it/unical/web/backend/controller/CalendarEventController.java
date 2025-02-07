package it.unical.web.backend.controller;

import it.unical.web.backend.controller.DTO.CalendarDTO;
import it.unical.web.backend.persistence.model.CalendarEvent;
import it.unical.web.backend.persistence.model.Tag;
import it.unical.web.backend.service.CalendarEventService;
import it.unical.web.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CalendarDTO>> getCalendarEvents() {
        CalendarEventService calendarEventService = new CalendarEventService();
        UserService userService = new UserService();
        int userId = userService.getCurrentUserIdByUsername();
        return ResponseEntity.ok().body(calendarEventService.getAllEvents(userService.getUserById(userId)));
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createCalendarEvent(@RequestBody Map<String, Object> payload) {
        CalendarEvent calendarEvent = new CalendarEvent();
//        export interface CalendarEvent {
//            id: number
//            categoryId?: number
//            title: string
//            description?: string
//            start: DateTime //Formato ISO + ORA
//            end: DateTime //Formato ISO + ORA
//            tags: number[]
//        }
        UserService userService = new UserService();
        int userId = userService.getCurrentUserIdByUsername();
        calendarEvent.setUserId(userService.getUserById(userId));

        calendarEvent.setTitle((String)payload.get("title"));
        calendarEvent.setDescription((String)payload.get("description"));
        String startDateString = (String) payload.get("start");
        String endDateString = (String) payload.get("end");

        System.out.println("Start: " + startDateString + " End: " + endDateString);

        List<Integer> tags = (List<Integer>) payload.get("tags");
        System.out.println("Tags:"+tags);
        List<Tag> tagforNote = new ArrayList<>();
        for(Integer t : tags) {
            Tag tag = new Tag();
            tag.setId(t);
            tagforNote.add(tag);
        }
        calendarEvent.setTags(tagforNote);

        // Parsing delle stringhe in LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime startDateTime = LocalDateTime.parse(startDateString, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateString, formatter);


        // Imposta le date nell'oggetto calendarEvent
        calendarEvent.setStart(startDateTime);
        calendarEvent.setEnd(endDateTime);

        calendarEventService.addEvent(calendarEvent);

        System.out.println("Calendar: "+calendarEvent.toString());
        return ResponseEntity.ok().body(calendarEvent.toString());
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteCalendarEvent(@RequestParam int id ) {
        CalendarEventService calendarEventService = new CalendarEventService();
        UserService userService = new UserService();
        int userId = userService.getCurrentUserIdByUsername();
        calendarEventService.deleteEvent(id);
        return ResponseEntity.ok().body(calendarEventService.toString());
    }

}
