package it.unical.web.backend.persistence.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MoodTracker {
    private int id;
    private User user;
    private int moodLevel;
    private LocalDate moodDate;
    private String notes;
    private List<Tag> tags;

    // Getters and Setters
}
