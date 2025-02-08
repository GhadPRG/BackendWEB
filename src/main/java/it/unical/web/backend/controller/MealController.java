package it.unical.web.backend.controller;


import it.unical.web.backend.persistence.dao.MealDAOImpl;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;
import it.unical.web.backend.persistence.proxy.MealProxy;
import it.unical.web.backend.service.DishInfoService;
import it.unical.web.backend.service.DishService;
import it.unical.web.backend.service.MealService;
import it.unical.web.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

        List<Meal> meals = mealService.getAllMeals(userId);

        Map<String, Map<String, Object>> responseMap = new HashMap<>();

        for (Meal meal : meals) {
            String mealType = meal.getMealType();

            if (!responseMap.containsKey(mealType)) {
                Map<String, Object> mealData = new HashMap<>();
                mealData.put("id", meal.getId());
                mealData.put("type", mealType);
                mealData.put("dishes", new ArrayList<Map<String, Object>>());
                responseMap.put(mealType, mealData);
            }

            List<Dish> dishes = meal.getDishes();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dishList = (List<Map<String, Object>>) responseMap.get(mealType).get("dishes");

            for (Dish dish : dishes) {
                // Il DishInfo viene caricato LAZY qui (dish.getDishInfo() chiama il proxy)
                DishInfo dishInfo = dish.getDishInfo();

                Map<String, Object> dishMap = new HashMap<>();
                dishMap.put("unit", normalizeUnit(dish.getUnit()));
                dishMap.put("quantity", dish.getQuantity());
                dishMap.put("dishInfo", Map.of(
                        "name", dishInfo.getNome(),
                        "kcalories", dishInfo.getKcalories(),
                        "proteins", dishInfo.getProteins(),
                        "fats", dishInfo.getFats(),
                        "carbs", dishInfo.getCarbs(),
                        "fibers", dishInfo.getFibers()
                ));

                dishList.add(dishMap);
            }
        }

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

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
    public ResponseEntity<String> addToMeal(@RequestBody Map<String, Object> payload) {
        String meal_type = (String) payload.get("meal_type");
        String name = (String) payload.get("name");
        int kcalories = ((Number) payload.get("kcal")).intValue();
        int proteins = ((Number) payload.get("proteins")).intValue();
        int fats = ((Number) payload.get("fats")).intValue();
        int carbs = ((Number) payload.get("carbs")).intValue();
        int fibers = ((Number) payload.get("fibers")).intValue();
        int quantity = ((Number) payload.get("quantity")).intValue();
        String unit = (String) payload.get("unit");


        UserService userService = new UserService();
        int userId = userService.getCurrentUserIdByUsername();
        //Inserisco gli elementi in dishinfo
        DishInfo dishInfo=new DishInfo();
        dishInfo.setNome(name);
        dishInfo.setKcalories(kcalories);
        dishInfo.setProteins(proteins);
        dishInfo.setFats(fats);
        dishInfo.setCarbs(carbs);
        dishInfo.setFibers(fibers);
        dishInfo.setCreatedBy(userService.getUserById(userId));
        DishInfoService dishInfoService = new DishInfoService();
        int idDishInfo=dishInfoService.addDishInfo(dishInfo); //Aggiungo il dishinfo nel db e prendo l'id

        MealDAOImpl mealDAO = new MealDAOImpl();
        int meal_id = mealDAO.getMealByType(meal_type);

        if (meal_id < 0) {
            // Crea un nuovo MealProxy e assegna i dati
            MealProxy mealProxy = new MealProxy();
            mealProxy.setMealType(meal_type);
            mealProxy.setUser(userService.getUserById(userId));
            mealProxy.setMealDate(LocalDate.now());

            // Inserisce il pasto e ottieni l'ID generato
            meal_id = mealDAO.createMeal(mealProxy);

            if (meal_id == -1) {
                return new ResponseEntity<>("Errore nella creazione del pasto", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Meal meal = mealDAO.getMealById(meal_id);

        Dish dish = new Dish();
        dish.setDishInfo(dishInfo);
        dish.setMeal(meal);
        dish.setQuantity(quantity);
        dish.setUnit(unit);

        DishService dishService = new DishService();
        dishService.addDish(dish);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
