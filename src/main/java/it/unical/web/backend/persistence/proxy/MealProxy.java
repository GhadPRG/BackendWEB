package it.unical.web.backend.persistence.proxy;


import it.unical.web.backend.persistence.dao.DishDAOImpl;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.Meal;

import java.util.List;

public class MealProxy extends Meal {
    private boolean dishesLoaded = false;

    @Override
    public List<Dish> getDishes() {
        if (!dishesLoaded) {
            super.setDishes(new DishDAOImpl().getAllDishesByMealId(this.getId()));
            dishesLoaded = true;
        }
        return super.getDishes();
    }
}