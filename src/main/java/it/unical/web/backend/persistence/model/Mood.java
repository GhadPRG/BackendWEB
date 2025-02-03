package it.unical.web.backend.persistence.model;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Mood {
    private long id;
    private long user;
    private int moodLevel;
    private LocalDate moodDate;
    private String notes;
    private Integer noteId; //Uso integer così può essere null visto che il mood nelle note è opzionale.
}
