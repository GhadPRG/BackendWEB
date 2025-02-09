package it.unical.web.backend.persistence.proxy;

import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;


import it.unical.web.backend.persistence.dao.DishInfoDAOImpl;

public class DishProxy extends Dish {
    private boolean dishInfoLoaded = false;

    @Override
    public DishInfo getDishInfo() {
        if (!dishInfoLoaded) {
            DishInfo dishInfo = new DishInfoDAOImpl().getDishInfoById(super.getDishInfo().getId());
            super.setDishInfo(dishInfo);
            dishInfoLoaded = true;
        }
        return super.getDishInfo();
    }
}