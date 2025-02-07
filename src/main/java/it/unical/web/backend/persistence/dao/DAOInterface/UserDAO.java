package it.unical.web.backend.persistence.dao.DAOInterface;
import it.unical.web.backend.persistence.model.User;


public interface UserDAO {
    User getUserById(int id);
    User getUserByUsername(String username);
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
}
