package it.unical.web.backend.persistence.dao.DAOInterface;
import it.unical.web.backend.persistence.model.User;

import java.util.List;

public interface UserDAO{

    List<User> getAll();
    User getById(int id);
    void add(User entity);
    void update(User entity);
    void delete(int id);
}
