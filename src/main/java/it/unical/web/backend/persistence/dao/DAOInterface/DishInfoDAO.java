package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;

import java.util.List;

public interface DishInfoDAO {
    DishInfo getDishInfoById(int id);
    List<DishInfo> getAllDishInfosByUser(int userId);
    void createDishInfo(DishInfo dishInfo);
    void updateDishInfo(DishInfo dishInfo);
    void deleteDishInfo(int id);
}