package it.unical.web.backend.persistence.model;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Meal {
    private long mealId;
    private long userId;
    private String mealType;
    private LocalDate mealDate;
    private long dishInfoId;
    DishInfo dishInfo;
    private long dishId;
    private int quantity;
}
