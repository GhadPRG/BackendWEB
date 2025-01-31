package it.unical.web.backend.Persistence.Model;
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
