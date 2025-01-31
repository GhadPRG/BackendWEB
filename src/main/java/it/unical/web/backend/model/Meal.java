package it.unical.web.backend.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Meal {
    private long id;
    private User user;
    private Date date;
    private int kcalories;
    private int carbs;
    private int proteins;
    private int fats;
    private int fibers;
}
