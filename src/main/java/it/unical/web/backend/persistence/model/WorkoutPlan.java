package it.unical.web.backend.persistence.model;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WorkoutPlan {
    private Long id;
    private String name;
    private String description;
    private Long created_by;

    public WorkoutPlan(String name, String description, Long created_by) {
        this.name = name;
        this.description = description;
        this.created_by = created_by;
    }
}
