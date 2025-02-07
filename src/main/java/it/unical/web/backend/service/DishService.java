package it.unical.web.backend.service;


import it.unical.web.backend.persistence.dao.DishDAOImpl;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;
import org.springframework.stereotype.Service;

@Service
public class DishService {

    public void addDish(Dish dish) {
        DishDAOImpl dishDAO = new DishDAOImpl();
        dishDAO.createDish(dish);
    }
}
