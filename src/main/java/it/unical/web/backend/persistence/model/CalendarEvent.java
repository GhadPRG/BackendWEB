package it.unical.web.backend.persistence.model;


import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CalendarEvent {
    private int id;
    private User userId;
    private Integer categoryId; // Può essere null
    private String title;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Tag> tags;


    public List<Integer> tagsToInt() {
        List<Integer> tagsID = new ArrayList<>();
        for (Tag tag : this.tags) {
            tagsID.add(tag.getId());
        }
        return tagsID;
    }
}
