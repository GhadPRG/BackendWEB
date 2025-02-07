package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.DAOInterface.DishInfoDAO;
import it.unical.web.backend.persistence.dao.DishInfoDAOImpl;
import it.unical.web.backend.persistence.model.DishInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishInfoService {



    public int addDishInfo(DishInfo dishInfo) {
        DishInfoDAOImpl dishInfoDAO = new DishInfoDAOImpl();
        return dishInfoDAO.createDishInfo(dishInfo);
    }
}
