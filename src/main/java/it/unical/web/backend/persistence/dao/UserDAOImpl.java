package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.dao.DAOInterface.UserDAO;
import it.unical.web.backend.persistence.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static User mapResultToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getDate("birth").toLocalDate(),
                rs.getString("gender"),
                rs.getInt("height"),
                rs.getInt("weight"),
                rs.getInt("daily_kcalories")
        );
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM \"User\" u LEFT JOIN \"UserInfo\" ui ON ui.user_id = u.id";
        List<User> users = new ArrayList<User>();
        try(Connection dbConnection=DatabaseConnection.getConnection();
            PreparedStatement statement= dbConnection.prepareStatement(query);
            ResultSet resultSet= statement.executeQuery()) {
            while(resultSet.next()) {
                User user = mapResultToUser(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Eccezione in getAll (UserDAO)"+e.getMessage());

        }
        System.out.println("Trovati: "+users.size()+" utenti");
        return users;
    }

    @Override
    public User getById(int id) {
        String query = "SELECT * FROM \"User\" u LEFT JOIN \"UserInfo\" ui ON u.id = ui.user_id WHERE u.id = ?";
        User user = null;
        try(Connection dbConnection=DatabaseConnection.getConnection();
            PreparedStatement statement= dbConnection.prepareStatement(query);){
            statement.setInt(1, id);
            ResultSet resultSet= statement.executeQuery();
            if(resultSet.next()) {
                user = mapResultToUser(resultSet);
            }
        }catch (SQLException e){
            System.out.println("Eccezione in getById(UserDAO)"+e.getMessage());
        }
        return user;
    }

    //Visto che l'iscrizione
    @Override
    public void add(User entity) {
        String query= """
                WITH add_user AS (
                    INSERT INTO "User" (username, password)
                    VALUES (?,?)
                    RETURNING id
                )
                INSERT INTO "UserInfo" (user_id, first_name, last_name, email, birth, gender, height, weight, daily_kcalories)
                SELECT id, ?, ?, ?, ?, ?, ?, ?, ?
                FROM add_user;
                """;

        try(Connection dbConnection= DatabaseConnection.getConnection();
            PreparedStatement statement= dbConnection.prepareStatement(query)
        ) {

            System.out.println("Entity in add:"+entity.toString());

            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setString(3, entity.getFirstName());
            statement.setString(4, entity.getLastName());
            statement.setString(5, entity.getEmail());
            statement.setDate(6, java.sql.Date.valueOf(entity.getBirthDate()));
            statement.setString(7, entity.getGender());
            statement.setFloat(8, entity.getHeight());
            statement.setFloat(9, entity.getWeight());
            statement.setInt(10, entity.getDailyCalories());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eccezione in add(UserDAO)"+e.getMessage());
        }


    }


    //Visto che non è detto che ci sia bisogno di modificare TUTTO la riga di una persona
    //Bisogna creare la query in maniera dinamica, aggiungendo man mano gli elementi modificati.
    @Override
    public void update(User entity) {

        System.out.println("Entity: "+entity.toString());
        StringBuilder queryBuilder = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        // Inizia a costruire la query per aggiornare la tabella User
        queryBuilder.append("WITH updated_user AS (UPDATE \"User\" SET ");

        // Aggiungi username se è stato modificato
        if (entity.getUsername() != null) {
            queryBuilder.append("username = ?, ");
            parameters.add(entity.getUsername());
        }

        // Aggiungi password se è stata modificata
        if (entity.getPassword() != null) {
            queryBuilder.append("password = ?, ");
            parameters.add(entity.getPassword());
        }

        // Rimuovi l'ultima virgola e spazio
        if (queryBuilder.toString().endsWith(", ")) {
            queryBuilder.setLength(queryBuilder.length() - 2);
        }

        // Completa la query per la tabella User
        queryBuilder.append(" WHERE id = ? RETURNING id) ");
        parameters.add(entity.getId());

        // Inizia a costruire la query per aggiornare la tabella UserInfo
        queryBuilder.append("UPDATE \"UserInfo\" SET ");

        // Aggiungi first_name se è stato modificato
        if (entity.getFirstName() != null) {
            queryBuilder.append("first_name = ?, ");
            parameters.add(entity.getFirstName());
        }

        // Aggiungi last_name se è stato modificato
        if (entity.getLastName() != null) {
            queryBuilder.append("last_name = ?, ");
            parameters.add(entity.getLastName());
        }

        // Aggiungi email se è stata modificata
        if (entity.getEmail() != null) {
            queryBuilder.append("email = ?, ");
            parameters.add(entity.getEmail());
        }

        // Aggiungi birth se è stato modificato
        if (entity.getBirthDate() != null) {
            queryBuilder.append("birth = ?, ");
            parameters.add(java.sql.Date.valueOf(entity.getBirthDate()));
        }

        // Aggiungi gender se è stato modificato
        if (entity.getGender() != null) {
            queryBuilder.append("gender = ?, ");
            parameters.add(entity.getGender());
        }

        // Aggiungi height se è stato modificato
        if (entity.getHeight() != 0) { // Supponendo che 0 sia un valore non valido per l'altezza
            queryBuilder.append("height = ?, ");
            parameters.add(entity.getHeight());
        }

        // Aggiungi weight se è stato modificato
        if (entity.getWeight() != 0) { // Supponendo che 0 sia un valore non valido per il peso
            queryBuilder.append("weight = ?, ");
            parameters.add(entity.getWeight());
        }

        // Aggiungi daily_kcalories se è stato modificato
        if (entity.getDailyCalories() != 0) { // Supponendo che 0 sia un valore non valido per le calorie
            queryBuilder.append("daily_kcalories = ?, ");
            parameters.add(entity.getDailyCalories());
        }

        // Rimuovi l'ultima virgola e spazio
        if (queryBuilder.toString().endsWith(", ")) {
            queryBuilder.setLength(queryBuilder.length() - 2);
        }

        // Completa la query per la tabella UserInfo
        queryBuilder.append(" WHERE user_id = (SELECT id FROM updated_user);");

        // Esegui la query
        try (Connection dbConnection = DatabaseConnection.getConnection();
             PreparedStatement statement = dbConnection.prepareStatement(queryBuilder.toString())) {

            // Imposta i parametri dinamici
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            // Esegui l'aggiornamento
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Righe aggiornate: " + rowsUpdated);

        } catch (SQLException e) {
            System.out.println("Eccezione in update(UserDAO)"+e.getMessage());

        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM \"User\" WHERE id = ?";
        try(Connection dbConnection=DatabaseConnection.getConnection();
        PreparedStatement statement= dbConnection.prepareStatement(query)){
            statement.setInt(1, id);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Righe eliminate: " + rowsUpdated);
        } catch (SQLException e) {
            System.out.println("Eccezione in delete(UserDAO)"+e.getMessage());

        }
    }

    public static User getByUsername(String username) {
        String query = "SELECT * FROM \"User\" u LEFT JOIN \"UserInfo\" ui ON u.id = ui.user_id WHERE u.username = ?";
        User user = null;
        try(Connection dbConnection = DatabaseConnection.getConnection();
            PreparedStatement statement = dbConnection.prepareStatement(query);){
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                user = mapResultToUser(rs);
            }
        }catch (SQLException e){
            System.out.println("Eccezione in getByUsername(UserDAO)");
            throw new RuntimeException(e);
        }
        return user;
    }

    public boolean isEmailUnique(String email) {
        String query = "SELECT email FROM \"UserInfo\" WHERE email = ?;";
        try(Connection dbConnection = DatabaseConnection.getConnection(); PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            return !rs.next();
        }catch (SQLException e){
            System.out.println("Eccezione in isEmailUnique(UserDAO)");
            throw new RuntimeException(e);
        }
    }

}
