package it.unical.web.backend.service;


import it.unical.web.backend.persistence.dao.DAOInterface.MealDAO;
import it.unical.web.backend.persistence.dao.DAOInterface.UserDAO;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MealService {

    private final MealDAO mealDAO;


    @Autowired
    public MealService(MealDAO mealDAO) {
        this.mealDAO = mealDAO;
    }

    public List<Meal> getAllMeals(int userId) {
        List<Meal> meal= mealDAO.getAllMealsByUser(userId);
        for(Meal m: meal){
            System.out.println("Meal Type: "+m.getMealType()+" mealDate: "+m.getMealDate());
            for(Dish d: m.getDishes()){
                System.out.println(" Quantità:"+d.getQuantity()+" Info Piatto: "+d.getDishInfo());
            }
        }
        return mealDAO.getAllMealsByUser(userId);
    }

    public int deleteMeal(int mealId) {
        return mealDAO.deleteMeal(mealId);
    }


    public int createMeal(Meal meal) {
        return mealDAO.createMeal(meal);
    }
}
