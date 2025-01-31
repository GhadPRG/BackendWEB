package it.unical.web.backend.model;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Note {
    private long id;
    private User user;
    private Category category;
    private String title;
    private String description;
}
