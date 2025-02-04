package it.unical.web.backend.persistence.model;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Tag {
    private int id;
    private Category category;
    private String name;
    private String description;

    // Getters and Setters
}