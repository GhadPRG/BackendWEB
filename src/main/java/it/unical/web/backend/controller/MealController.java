package it.unical.web.backend.controller;


import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.Meal;
import it.unical.web.backend.service.MealService;
import it.unical.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meal")
public class MealController {
    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Map<String, Object>>> getMeals() {
        UserService userService = new UserService();
        int userId = userService.getCurrentUserIdByUsername();
        System.out.println("UserID: " + userId);

        // Recupera tutti i pasti per l'utente corrente
        List<Meal> meals = mealService.getAllMeals(userId);

        // Crea una mappa per raggruppare i pasti per tipo (Breakfast, Dinner, Lunch)
        Map<String, Map<String, Object>> responseMap = new HashMap<>();

        for (Meal meal : meals) {
            String mealType = meal.getMealType();

            // Se il tipo di pasto non è già nella mappa, inizializzalo
            if (!responseMap.containsKey(mealType)) {
                Map<String, Object> mealData = new HashMap<>();
                mealData.put("id", meal.getId()); // Aggiungi meal_id
                mealData.put("type", mealType);
                mealData.put("dishes", new ArrayList<Map<String, Object>>());
                responseMap.put(mealType, mealData);
            }

            // Ottieni la lista dei piatti per questo tipo di pasto
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dishes = (List<Map<String, Object>>) responseMap.get(mealType).get("dishes");

            // Aggiungi ogni piatto alla lista
            for (Dish dish : meal.getDishes()) {
                Map<String, Object> dishMap = new HashMap<>();
                dishMap.put("unit", normalizeUnit(dish.getUnit())); // Normalizza l'unità di misura
                dishMap.put("quantity", dish.getQuantity());
                dishMap.put("dishInfo", Map.of(
                        "name", dish.getDishInfo().getNome(),
                        "kcalories", dish.getDishInfo().getKcalories(),
                        "proteins", dish.getDishInfo().getProteins(),
                        "fats", dish.getDishInfo().getFats(),
                        "carbs", dish.getDishInfo().getCarbs(),
                        "fibers", dish.getDishInfo().getFibers()
                ));
                dishes.add(dishMap);
            }
        }

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    // Metodo per normalizzare le unità di misura
    private String normalizeUnit(String unit) {
        switch (unit.toLowerCase()) {
            case "oz":
                return "grams";
            case "piatti":
                return "plates";
            case "unità":
                return "units";
            default:
                return unit;
        }
    }

    //Cancella il pasto all'interno di meal, ogni piatto collegato viene eliminato (non dal dishinfo)
    //Ad esempio, se nel pasto con id=1 (Pranzo) ho 3 piatti di pasta nella tabella dishes, i piatti vengono in quella
    //tabella vengono eliminati da quella tabella. Eliminando il pasto quindi si eliminano le relazioni con i piatti
    //collegati a quel pasto
    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteMeal(@RequestParam int id_meal) {
        if(mealService.deleteMeal(id_meal)>0){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addToMeal(@RequestParam int meal_id,
                                          @RequestParam int meal_type,
                                          @RequestParam String name,
                                          @RequestParam float kcalories,
                                          @RequestParam float proteins,
                                          @RequestParam float fats,
                                          @RequestParam int carbs,
                                          @RequestParam int fibers,
                                          @RequestParam int quantity,
                                          @RequestParam int unit) {





        return new ResponseEntity<>(HttpStatus.CREATED);
    }




}
