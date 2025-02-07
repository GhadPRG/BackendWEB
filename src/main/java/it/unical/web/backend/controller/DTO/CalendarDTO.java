package it.unical.web.backend.controller.DTO;

import it.unical.web.backend.persistence.model.CalendarEvent;
import it.unical.web.backend.persistence.model.Tag;
import it.unical.web.backend.persistence.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CalendarDTO {
    private int id;
    private User userId;
    private Integer categoryId; // Può essere null
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Integer> tags;


    public CalendarDTO(CalendarEvent calendar) {
        this.id = calendar.getId();
        this.userId = calendar.getUserId();
        this.categoryId = calendar.getCategoryId();
        this.title = calendar.getTitle();
        this.description = calendar.getDescription();
        this.start = calendar.getStart();
        this.end = calendar.getEnd();
        this.tags = calendar.tagsToInt();
    }

    public List<CalendarDTO> GetCalendarDTOS(List<CalendarEvent> calendars) {
        List<CalendarDTO> calendarDTOS = new ArrayList<>();
        for (CalendarEvent calendar : calendars) {
            calendarDTOS.add(new CalendarDTO(calendar));
        }
        return calendarDTOS;
    }
}
