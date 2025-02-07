package it.unical.web.backend.persistence.dao;
import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.DishDAO;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DishDAOImpl implements DishDAO {
    Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public Dish getDishById(int id) {
        String query = "SELECT * FROM dishes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));

                // Fetch meal
                Meal meal = new Meal();
                meal.setId(rs.getInt("meal_id")); // Assume the meal is already fetched elsewhere
                dish.setMeal(meal);

                // Fetch dish info
                DishInfo dishInfo = new DishInfoDAOImpl().getDishInfoById(rs.getInt("dish_info_id"));
                dish.setDishInfo(dishInfo);

                dish.setQuantity(rs.getInt("quantity"));
                dish.setUnit(rs.getString("unit"));

                return dish;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Dish> getAllDishesByMealId(int mealId) {
        List<Dish> dishes = new ArrayList<>();
        String query = "SELECT * FROM dishes WHERE meal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, mealId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));

                // Fetch meal
                Meal meal = new Meal();
                meal.setId(mealId); // Assume the meal is already fetched elsewhere
                dish.setMeal(meal);

                // Fetch dish info
                DishInfo dishInfo = new DishInfoDAOImpl().getDishInfoById(rs.getInt("dish_info_id"));
                dish.setDishInfo(dishInfo);

                dish.setQuantity(rs.getInt("quantity"));
                dish.setUnit(rs.getString("unit"));

                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dishes;
    }

    @Override
    public void createDish(Dish dish) {
        String query = "INSERT INTO dishes (meal_id, dish_info_id, quantity, unit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, dish.getMeal().getId());
            stmt.setInt(2, dish.getDishInfo().getId());
            stmt.setInt(3, dish.getQuantity());
            stmt.setString(4, dish.getUnit());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                dish.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDish(Dish dish) {
        String query = "UPDATE dishes SET meal_id = ?, dish_info_id = ?, quantity = ?, unit=? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, dish.getMeal().getId());
            stmt.setInt(2, dish.getDishInfo().getId());
            stmt.setInt(3, dish.getQuantity());
            stmt.setString(4, dish.getUnit());
            stmt.setInt(5, dish.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDish(int id) {
        String query = "DELETE FROM dishes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}