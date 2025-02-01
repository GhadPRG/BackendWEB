package it.unical.web.backend.persistence.model;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class DailyMood {
    private long id;
    private User user;
    private Category category;
    private Date date;
    private String description;
}
