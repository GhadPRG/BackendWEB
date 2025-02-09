package it.unical.web.backend.service;


import it.unical.web.backend.persistence.dao.DAOInterface.MealDAO;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    private final MealDAO mealDAO;


    @Autowired
    public MealService(MealDAO mealDAO) {
        this.mealDAO = mealDAO;
    }

    public List<Meal> getAllMeals(int userId) {
        return mealDAO.getAllMealsByUser(userId);
    }

    public int deleteMeal(int mealId) {
        return mealDAO.deleteMeal(mealId);
    }

    public int createMeal(Meal meal) {
        return mealDAO.createMeal(meal);
    }
}
