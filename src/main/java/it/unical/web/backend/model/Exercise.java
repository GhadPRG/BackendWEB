package it.unical.web.backend.model;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Exercise {
    private long id;
    private User user;
    private Date date;
    private int sets;
    private int reps;
    private int weightUsed;
    private int timePassed;
    private String name;
    private String targetMuscleGroup;
    private int kcaloriesBurnedPerRep;
}
