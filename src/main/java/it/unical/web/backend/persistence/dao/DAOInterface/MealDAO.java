package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Meal;
import java.util.List;

public interface MealDAO {
    Meal getMealById(int id);
    List<Meal> getAllMealsByUser(int userId);
    void createMeal(Meal meal);
    void updateMeal(Meal meal);
    void deleteMeal(int id);
}
