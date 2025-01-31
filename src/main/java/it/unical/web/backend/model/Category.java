package it.unical.web.backend.model;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Category {
    private long id;
    private String name;
    private String tag;
    private String description;
}
