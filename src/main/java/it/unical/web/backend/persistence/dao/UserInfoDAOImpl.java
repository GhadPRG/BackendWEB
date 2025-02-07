package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.UserInfoDAO;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.UserInfo;

import java.sql.*;

public class UserInfoDAOImpl implements UserInfoDAO {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    public UserInfo getUserInfoByUsername(String username) {
        String query = "SELECT ui.* " +
                "FROM \"UserInfo\" ui " +
                "JOIN \"User\" u ON ui.user_id = u.id " +
                "WHERE u.username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserInfo userInfo = new UserInfo();
                User user = new User();
                user.setId(rs.getInt("user_id")); // Assume the user is already fetched elsewhere
                userInfo.setUser(user);

                userInfo.setFirstName(rs.getString("first_name"));
                userInfo.setLastName(rs.getString("last_name"));
                userInfo.setEmail(rs.getString("email"));
                userInfo.setBirth(rs.getDate("birth").toLocalDate());
                userInfo.setGender(rs.getString("gender"));
                userInfo.setHeight(rs.getFloat("height"));
                userInfo.setWeight(rs.getFloat("weight"));
                userInfo.setDailyKcalories(rs.getInt("daily_kcalories"));

                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public UserInfo getUserInfoByUserId(int userId) {
        String query = "SELECT * FROM \"UserInfo\" WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserInfo userInfo = new UserInfo();
                User user = new User();
                user.setId(userId); // Assume the user is already fetched elsewhere
                userInfo.setUser(user);
                userInfo.setFirstName(rs.getString("first_name"));
                userInfo.setLastName(rs.getString("last_name"));
                userInfo.setEmail(rs.getString("email"));
                userInfo.setBirth(rs.getDate("birth").toLocalDate());
                userInfo.setGender(rs.getString("gender"));
                userInfo.setHeight(rs.getFloat("height"));
                userInfo.setWeight(rs.getFloat("weight"));
                userInfo.setDailyKcalories(rs.getInt("daily_kcalories"));
                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void createUserInfo(UserInfo userInfo) {
        String query = "INSERT INTO \"UserInfo\" (user_id, first_name, last_name, email, birth, gender, height, weight, daily_kcalories) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userInfo.getUser().getId());
            stmt.setString(2, userInfo.getFirstName());
            stmt.setString(3, userInfo.getLastName());
            stmt.setString(4, userInfo.getEmail());
            stmt.setDate(5, Date.valueOf(userInfo.getBirth()));
            stmt.setString(6, userInfo.getGender());
            stmt.setFloat(7, userInfo.getHeight());
            stmt.setFloat(8, userInfo.getWeight());
            stmt.setInt(9, userInfo.getDailyKcalories());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) {
        String query = "UPDATE \"UserInfo\" SET first_name = ?, last_name = ?, email = ?, birth = ?, gender = ?, height = ?, weight = ?, daily_kcalories = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userInfo.getFirstName());
            stmt.setString(2, userInfo.getLastName());
            stmt.setString(3, userInfo.getEmail());
            stmt.setDate(4, Date.valueOf(userInfo.getBirth()));
            stmt.setString(5, userInfo.getGender());
            stmt.setFloat(6, userInfo.getHeight());
            stmt.setFloat(7, userInfo.getWeight());
            stmt.setInt(8, userInfo.getDailyKcalories());
            stmt.setInt(9, userInfo.getUser().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmailUnique(String email) {
        String query = "SELECT email FROM \"UserInfo\" WHERE email = ?;";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            return !rs.next();
        }catch (SQLException e){
            System.out.println("Eccezione in isEmailUnique(UserDAO)");
            throw new RuntimeException(e);
        }
    }
}
