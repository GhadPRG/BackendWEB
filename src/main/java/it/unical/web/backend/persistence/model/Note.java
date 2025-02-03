package it.unical.web.backend.persistence.model;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Note {
    private long id;
    private long user;
    private String title;
    private String content;
    private LocalDate created_at;

}
