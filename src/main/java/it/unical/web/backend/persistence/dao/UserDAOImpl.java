package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.UserDAO;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.proxy.UserProxy;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    final Connection connection= DatabaseConnection.getInstance().getConnection();

    public UserDAOImpl(){}

    @Override
    public User getUserById(int id) {
        String query = "SELECT * FROM \"User\" WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserProxy proxy = new UserProxy(); // Usa il proxy
                proxy.setId(rs.getInt("id"));
                proxy.setUsername(rs.getString("username"));
                proxy.setPassword(rs.getString("password"));
                return proxy;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM \"User\" WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserProxy proxy = new UserProxy(); // Usa il proxy
                proxy.setId(rs.getInt("id"));
                proxy.setUsername(rs.getString("username"));
                proxy.setPassword(rs.getString("password"));
                return proxy;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createUser(User user) {
        String query = "INSERT INTO \"User\" (username, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE \"User\" SET username = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int id) {
        String query = "DELETE FROM \"User\" WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUsernameUnique(String username) {
        String query = "SELECT username FROM \"User\" WHERE username = ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return !rs.next();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}