package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.MealDAO;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.Meal;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.proxy.MealProxy;
import it.unical.web.backend.persistence.proxy.UserProxy;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MealDAOImpl implements MealDAO {
    private final Connection connection= DatabaseConnection.getInstance().getConnection();

    @Override
    public Meal getMealById(int id) {
        String query = "SELECT * FROM meals WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Meal meal = new Meal();
                meal.setId(rs.getInt("id"));

                User user = new User();
                user.setId(rs.getInt("user_id")); // Assume the user is already fetched elsewhere
                meal.setUser(user);

                meal.setMealType(rs.getString("meal_type"));
                meal.setMealDate(rs.getDate("meal_date").toLocalDate());

                meal.setDishes(new DishDAOImpl().getAllDishesByMealId(id));

                return meal;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Meal> getAllMealsByUser(int userId) {
        List<Meal> meals = new ArrayList<>();
        String query = "SELECT * FROM meals WHERE user_id = ? AND meal_date=CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MealProxy mealProxy = new MealProxy();
                mealProxy.setId(rs.getInt("id"));

                UserProxy userProxy = new UserProxy();
                userProxy.setId(rs.getInt("user_id")); // Solo l'ID, senza caricare altri dati
                mealProxy.setUser(userProxy);

                mealProxy.setMealType(rs.getString("meal_type"));
                mealProxy.setMealDate(rs.getDate("meal_date").toLocalDate());

                meals.add(mealProxy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }


    public int createMealWithDishes(Meal meal) {
        String query = "INSERT INTO meals (user_id, meal_type, meal_date) VALUES (?, ?, CURRENT_DATE) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, meal.getUser().getId());
            stmt.setString(2, meal.getMealType());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                meal.setId(rs.getInt(1));
            }

            for (Dish dish : meal.getDishes()) {
                new DishDAOImpl().createDish(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meal.getId();
    }

    @Override
    public int createMeal(Meal meal) {
        String query = "INSERT INTO meals (user_id, meal_type, meal_date) VALUES (?, ?, CURRENT_DATE) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, meal.getUser().getId());
            stmt.setString(2, meal.getMealType());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                meal.setId(generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }




    @Override
    public void updateMeal(Meal meal) {
        String query = "UPDATE meals SET user_id = ?, meal_type = ?, meal_date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, meal.getUser().getId());
            stmt.setString(2, meal.getMealType());
            stmt.setDate(3, Date.valueOf(meal.getMealDate()));
            stmt.setInt(4, meal.getId());
            stmt.executeUpdate();

            for (Dish dish : meal.getDishes()) {
                new DishDAOImpl().updateDish(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int deleteMeal(int id) {
        String query = "DELETE FROM meals WHERE id = ?";
        int row=0;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            row=stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row;
    }

    public int getMealByType(String mealType, int user_id) {
        String query = "SELECT id FROM meals WHERE meal_type = ? AND meal_date = CURRENT_DATE AND user_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mealType);
            stmt.setInt(2, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}