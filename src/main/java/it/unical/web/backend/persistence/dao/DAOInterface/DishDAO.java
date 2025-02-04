package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Dish;

import java.util.List;

public interface DishDAO {
    Dish getDishById(int id);

    List<Dish> getAllDishesByMealId(int mealId);

    void createDish(Dish dish);
    void updateDish(Dish dish);
    void deleteDish(int id);
}