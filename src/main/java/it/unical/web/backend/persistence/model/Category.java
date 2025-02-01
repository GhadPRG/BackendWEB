package it.unical.web.backend.persistence.model;
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
