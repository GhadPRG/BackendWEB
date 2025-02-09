package it.unical.web.backend.persistence.proxy;


import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.User;

public class DishInfoProxy extends DishInfo {
    private boolean createdByLoaded = false;

    @Override
    public User getCreatedBy() {
        if (!createdByLoaded) {
            User user = new UserDAOImpl().getUserById(super.getCreatedBy().getId());
            super.setCreatedBy(user);
            createdByLoaded = true;
        }
        return super.getCreatedBy();
    }
}