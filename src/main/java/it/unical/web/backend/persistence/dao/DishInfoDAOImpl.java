package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.DishInfoDAO;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishInfoDAOImpl implements DishInfoDAO {
    private final Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public DishInfo getDishInfoById(int id) {
        String query = "SELECT * FROM dish_info WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                DishInfo dishInfo = new DishInfo();
                dishInfo.setId(rs.getInt("id"));
                dishInfo.setNome(rs.getString("nome"));
                dishInfo.setKcalories(rs.getInt("kcalories"));
                dishInfo.setCarbs(rs.getInt("carbs"));
                dishInfo.setProteins(rs.getInt("proteins"));
                dishInfo.setFats(rs.getInt("fats"));
                dishInfo.setFibers(rs.getInt("fibers"));

                User createdBy = new User();
                createdBy.setId(rs.getInt("user_id")); // Assume the user is already fetched elsewhere
                dishInfo.setCreatedBy(createdBy);

                return dishInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DishInfo> getAllDishInfosByUser(int userId) {
        List<DishInfo> dishInfos = new ArrayList<>();
        String query = "SELECT * FROM dish_info WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DishInfo dishInfo = new DishInfo();
                dishInfo.setId(rs.getInt("id"));
                dishInfo.setNome(rs.getString("nome"));
                dishInfo.setKcalories(rs.getInt("kcalories"));
                dishInfo.setCarbs(rs.getInt("carbs"));
                dishInfo.setProteins(rs.getInt("proteins"));
                dishInfo.setFats(rs.getInt("fats"));
                dishInfo.setFibers(rs.getInt("fibers"));

                User createdBy = new User();
                createdBy.setId(userId); // Assume the user is already fetched elsewhere
                dishInfo.setCreatedBy(createdBy);

                dishInfos.add(dishInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishInfos;
    }

    @Override
    public int createDishInfo(DishInfo dishInfo) {
        String query = "INSERT INTO dish_info (nome, kcalories, carbs, proteins, fats, fibers, user_id) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dishInfo.getNome());
            stmt.setInt(2, dishInfo.getKcalories());
            stmt.setInt(3, dishInfo.getCarbs());
            stmt.setInt(4, dishInfo.getProteins());
            stmt.setInt(5, dishInfo.getFats());
            stmt.setInt(6, dishInfo.getFibers());
            stmt.setInt(7, dishInfo.getCreatedBy().getId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                dishInfo.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishInfo.getId();
    }

    @Override
    public void updateDishInfo(DishInfo dishInfo) {
        String query = "UPDATE dish_info SET nome = ?, kcalories = ?, carbs = ?, proteins = ?, fats = ?, fibers = ?, user_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, dishInfo.getNome());
            stmt.setInt(2, dishInfo.getKcalories());
            stmt.setInt(3, dishInfo.getCarbs());
            stmt.setInt(4, dishInfo.getProteins());
            stmt.setInt(5, dishInfo.getFats());
            stmt.setInt(6, dishInfo.getFibers());
            stmt.setInt(7, dishInfo.getCreatedBy().getId());
            stmt.setInt(8, dishInfo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDishInfo(int id) {
        String query = "DELETE FROM dish_info WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
