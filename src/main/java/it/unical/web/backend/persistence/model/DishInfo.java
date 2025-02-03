package it.unical.web.backend.persistence.model;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DishInfo {
    private long id;
    private String nome;
    private int kcal;
    private int carbs;
    private int proteins;
    private int fats;
    private int fibers;
    private Long user_id;

    public DishInfo(String nome, int kcal, int carbs, int proteins, int fats, int fibers, Long user_id) {
        this.nome = nome;
        this.kcal = kcal;
        this.carbs = carbs;
        this.proteins = proteins;
        this.fats = fats;
        this.fibers = fibers;
        this.user_id = user_id;
    }
}
