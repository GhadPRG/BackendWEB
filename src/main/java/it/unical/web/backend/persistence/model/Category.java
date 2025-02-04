package it.unical.web.backend.persistence.model;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Category {
    private int id;
    private String name;
    private String description;
    private String color;
    private List<Tag> tags;

    // Getters and Setters
}