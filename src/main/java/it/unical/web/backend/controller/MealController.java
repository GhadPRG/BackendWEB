package it.unical.web.backend.controller;


import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.Meal;
import it.unical.web.backend.service.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getmeal")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<List<Map<String, Object>>>> getMeals(@RequestParam int id) {
        List<Meal> meals=mealService.getAllMeals(id);

        List<List<Map<String, Object>>> response=new ArrayList<>();
        for(Meal meal:meals){
            List<Map<String,Object>> dishMapFinal=new ArrayList<>();
            for(Dish dish:meal.getDishes()){
                Map<String,Object> dishMap=new HashMap<>();
                System.out.println("Inserisco il piatto:"+dish.getDishInfo().getNome());
                dishMap.put("mealData",meal.getMealDate());
                dishMap.put("mealType:",meal.getMealType());
                dishMap.put("name", dish.getDishInfo().getNome());
                dishMap.put("kcal", dish.getDishInfo().getKcalories());
                dishMap.put("carbs", dish.getDishInfo().getCarbs());
                dishMap.put("proteins", dish.getDishInfo().getProteins());
                dishMap.put("fats", dish.getDishInfo().getFats());
                dishMap.put("fibers", dish.getDishInfo().getFibers());
                dishMap.put("quantity", dish.getQuantity());
                dishMapFinal.add(dishMap);
            }
            response.add(dishMapFinal);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
