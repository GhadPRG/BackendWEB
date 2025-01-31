package it.unical.web.backend.model;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class DailyNote {
    private long id;
    private User user;
    private Date date;
    private String title;
    private String description;
}
