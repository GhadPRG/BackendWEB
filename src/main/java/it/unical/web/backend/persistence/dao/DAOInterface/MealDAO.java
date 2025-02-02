package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealDAO{

    List<Meal> getAll();
    List<DishInfo> getDishInfoByUserID(long id);
    List<Meal> getDishByDayTypeAndUserId(LocalDate date, String type, long id);
    void insertDishesForMeal(LocalDate date, String mealType, long userId, List<Dish> dishes);
    void insertDishInfo(DishInfo dishInfo);
    void updateDish(long dishId, Integer newQuantity, Long newDishInfoId);
    void updateDishInfo(DishInfo dishInfo);
    void delete(int id);
}
