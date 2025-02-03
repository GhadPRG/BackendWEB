package it.unical.web.backend.persistence.model;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Dish {
    private Long id;           // Corrisponde alla colonna "id" nella tabella dishes
    private Long mealId;       // Corrisponde alla colonna "meal_id"
    private Long dishInfoId;   // Corrisponde alla colonna "dish_info_id"
    private int quantity;      // Corrisponde alla colonna "quantity"

    // Costruttore senza id (utile per l'inserimento, poiché l'id è generato automaticamente)
    public Dish(Long mealId, Long dishInfoId, int quantity) {
        this.mealId = mealId;
        this.dishInfoId = dishInfoId;
        this.quantity = quantity;
    }
}