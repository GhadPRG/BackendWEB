package it.unical.web.backend.persistence.dao;

import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.model.Dish;
import it.unical.web.backend.persistence.model.DishInfo;
import it.unical.web.backend.persistence.model.Meal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealDAO implements DAO<Meal> {

    private Meal mapToMeal(ResultSet rs) throws SQLException {
        return new Meal(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("meal_type"),
                rs.getDate("meal_date").toLocalDate(),
                rs.getLong("dish_info_id"),
                new DishInfo(rs.getLong("dish_info_id"),
                        rs.getString("nome"),
                        rs.getInt("kcalories"),
                        rs.getInt("carbs"),
                        rs.getInt("proteins"),
                        rs.getInt("fats"),
                        rs.getInt("fibers"),
                        rs.getLong("user_id")),
                rs.getInt("dish_id"),
                rs.getInt("quantity"));
    }

    private DishInfo mapToDishInfo(ResultSet rs) throws SQLException {
        return new DishInfo(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getInt("kcalories"),
                rs.getInt("carbs"),
                rs.getInt("proteins"),
                rs.getInt("fats"),
                rs.getInt("fibers"),
                rs.getLong("user_id"));
    }

    @Override
    public List<Meal> getAll() {
        String query = """
                        SELECT * FROM meals m
                                 LEFT JOIN dishes d ON m.id = d.meal_id
                                 LEFT JOIN dish_info di ON d.dish_info_id = di.id  -- Cambia con l'ID dell'utente specifico
                        ORDER BY m.meal_date DESC, m.meal_type;""";
        List<Meal> meals = new ArrayList<>();
        try(Connection dbConnection= DatabaseConnection.getConnection();
            PreparedStatement statement=dbConnection.prepareStatement(query);
            ResultSet resultSet=statement.executeQuery()){
            while(resultSet.next()){
                meals.add(mapToMeal(resultSet));
            }
        }catch (SQLException e){
            System.out.println("Eccezione in getAll(MealDAO)"+e.getMessage());
        }
        System.out.println("Trovati "+meals.size()+" Piatti");
        return meals;
    }

    @Override
    public Meal getById(int mealId) {
        return null;
    }


    //Ottiengo dishinfo già registrati
    public List<DishInfo> getDishInfoByUserID(long id) {
        String query="SELECT * FROM \"dish_info\" WHERE user_id=?";
        List<DishInfo> dishInfos =new ArrayList<>();
        try(Connection dbConnection= DatabaseConnection.getConnection();
            PreparedStatement statement=dbConnection.prepareStatement(query)){
            statement.setLong(1, id);
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                dishInfos.add(mapToDishInfo(resultSet));
            }
        }catch (SQLException e){
            System.out.println("Eccezione in getDishInfoByUserID(MealDAO)"+e.getMessage());
        }
        if(dishInfos.isEmpty()){
            System.out.println("L'utente non ha piatti");
            return null;
        }else{
            System.out.println("Piatti per l'utente:"+ id + " trovati: "+ dishInfos.size());
            return dishInfos;
        }
    }

    public List<Meal> getDishByDayTypeAndUserId(LocalDate date, String type, long id) {
        String query = """
            SELECT
                m.id AS id,
                m.user_id,
                m.meal_type,
                m.meal_date,
                d.id AS dish_id,
                d.quantity,
                di.id AS dish_info_id,
                di.nome,
                di.kcalories,
                di.carbs,
                di.proteins,
                di.fats,
                di.fibers
            FROM
                meals m
            JOIN
                dishes d ON m.id = d.meal_id
            JOIN
                dish_info di ON d.dish_info_id = di.id
            WHERE
                m.user_id = ?
                AND m.meal_date = ?
                AND m.meal_type = ?""";

        List<Meal> meals = new ArrayList<>();

        try (Connection dbConnection = DatabaseConnection.getConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {

            // Imposta i parametri
            statement.setLong(1, id);
            statement.setDate(2, java.sql.Date.valueOf(date));  // Converti LocalDate in java.sql.Date
            statement.setString(3, type);

            System.out.println("Query: " + statement.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                meals.add(mapToMeal(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Eccezione in getDishByDayTypeAndUserId(MealDAO): " + e.getMessage());
        }

        if (meals.isEmpty()) {
            System.out.println("L'utente non ha piatti");
            return null;
        } else {
            System.out.println("Piatti per l'utente: " + id + " trovati: " + meals.size());
            return meals;
        }
    }

    public void insertDishesForMeal(LocalDate date, String mealType, long userId, List<Dish> dishes) {
        // Query per verificare se il pasto esiste già
        String checkMealQuery = """
            SELECT id
            FROM meals
            WHERE user_id = ?
              AND meal_date = ?
              AND meal_type = ?""";

        // Query per inserire un nuovo pasto
        String insertMealQuery = """
            INSERT INTO meals (user_id, meal_type, meal_date)
            VALUES (?, ?, ?)
            RETURNING id""";

        // Query per inserire un piatto (senza specificare l'id)
        String insertDishQuery = """
            INSERT INTO dishes (meal_id, dish_info_id, quantity)
            VALUES (?, ?, ?)""";

        try (Connection dbConnection = DatabaseConnection.getConnection();
             PreparedStatement checkMealStmt = dbConnection.prepareStatement(checkMealQuery);
             PreparedStatement insertMealStmt = dbConnection.prepareStatement(insertMealQuery);
             PreparedStatement insertDishStmt = dbConnection.prepareStatement(insertDishQuery)) {

            // Verifica se il pasto esiste già
            checkMealStmt.setLong(1, userId);
            checkMealStmt.setDate(2, java.sql.Date.valueOf(date));
            checkMealStmt.setString(3, mealType);

            ResultSet mealResultSet = checkMealStmt.executeQuery();
            long mealId;

            if (mealResultSet.next()) {
                // Se il pasto esiste, ottieni il suo ID
                mealId = mealResultSet.getLong("id");
            } else {
                // Se il pasto non esiste, creane uno nuovo
                insertMealStmt.setLong(1, userId);
                insertMealStmt.setString(2, mealType);
                insertMealStmt.setDate(3, java.sql.Date.valueOf(date));

                int affectedRows = insertMealStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creazione del pasto fallita, nessuna riga inserita.");
                }

                // Ottieni l'ID del pasto appena creato
                try (ResultSet generatedKeys = insertMealStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        mealId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creazione del pasto fallita, nessun ID ottenuto.");
                    }
                }
            }

            // Inserisci i piatti
            for (Dish dish : dishes) {
                insertDishStmt.setLong(1, mealId);           // meal_id
                insertDishStmt.setLong(2, dish.getDishInfoId());  // dish_info_id
                insertDishStmt.setInt(3, dish.getQuantity());     // quantity

                insertDishStmt.executeUpdate();
            }

            System.out.println("Piatti inseriti correttamente per il pasto: " + mealId);

        } catch (SQLException e) {
            System.out.println("Eccezione in insertDishesForMeal(MealDAO): " + e.getMessage());
        }
    }


    @Override
    public void add(Meal entity) {

    }

    public void insertDishInfo(DishInfo dishInfo) {
        String query = """
            INSERT INTO dish_info (nome, kcalories, carbs, proteins, fats, fibers, user_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)""";

        try (Connection dbConnection = DatabaseConnection.getConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {

            statement.setString(1, dishInfo.getNome());
            statement.setLong(2, dishInfo.getKcal());
            statement.setInt(3, dishInfo.getCarbs());
            statement.setInt(4, dishInfo.getProteins());
            statement.setInt(5, dishInfo.getFats());
            statement.setInt(6, dishInfo.getFibers());
            statement.setLong(7, dishInfo.getUser_id());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Nuovo DishInfo inserito con successo.");
            } else {
                System.out.println("Inserimento del DishInfo fallito.");
            }

        } catch (SQLException e) {
            System.out.println("Eccezione in insertDishInfo(MealDAO): " + e.getMessage());
        }
    }

    public void updateDish(long dishId, Integer newQuantity, Long newDishInfoId) {
        String query = """
            UPDATE dishes
            SET
                quantity = COALESCE(?, quantity),
                dish_info_id = COALESCE(?, dish_info_id)
            WHERE id = ?""";

        try (Connection dbConnection = DatabaseConnection.getConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {

            // Imposta i parametri
            if (newQuantity != null) {
                statement.setInt(1, newQuantity);
            } else {
                statement.setNull(1, Types.INTEGER);  // Non aggiornare la quantità
            }

            if (newDishInfoId != null) {
                statement.setLong(2, newDishInfoId);
            } else {
                statement.setNull(2, Types.BIGINT);  // Non aggiornare dish_info_id
            }

            statement.setLong(3, dishId);  // ID del piatto da aggiornare

            // Esegui l'aggiornamento
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Piatto aggiornato con successo.");
            } else {
                System.out.println("Nessun piatto trovato con l'ID specificato.");
            }

        } catch (SQLException e) {
            System.out.println("Eccezione in updateDish(MealDAO): " + e.getMessage());
        }
    }

    public void updateDishInfo(long dishInfoId, String nome, Integer kcalories, Integer carbs, Integer proteins, Integer fats, Integer fibers) {
        // Lista per memorizzare i campi da aggiornare
        List<String> updates = new ArrayList<>();
        // Lista per memorizzare i valori dei parametri
        List<Object> params = new ArrayList<>();

        // Aggiungi i campi da aggiornare alla lista
        if (nome != null) {
            updates.add("nome = ?");
            params.add(nome);
        }
        if (kcalories != null) {
            updates.add("kcalories = ?");
            params.add(kcalories);
        }
        if (carbs != null) {
            updates.add("carbs = ?");
            params.add(carbs);
        }
        if (proteins != null) {
            updates.add("proteins = ?");
            params.add(proteins);
        }
        if (fats != null) {
            updates.add("fats = ?");
            params.add(fats);
        }
        if (fibers != null) {
            updates.add("fibers = ?");
            params.add(fibers);
        }

        // Se non ci sono campi da aggiornare, esci dal metodo
        if (updates.isEmpty()) {
            System.out.println("Nessun campo da aggiornare.");
            return;
        }

        // Costruisci la query SQL dinamicamente
        String query = "UPDATE dish_info SET " + String.join(", ", updates) + " WHERE id = ?";

        try (Connection dbConnection = DatabaseConnection.getConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {

            // Imposta i parametri nella query
            int paramIndex = 1;
            for (Object param : params) {
                if (param instanceof String) {
                    statement.setString(paramIndex++, (String) param);
                } else if (param instanceof Integer) {
                    statement.setInt(paramIndex++, (Integer) param);
                }
            }

            // Imposta l'ID del piatto come ultimo parametro
            statement.setLong(paramIndex, dishInfoId);

            // Esegui l'aggiornamento
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Informazioni del piatto aggiornate con successo.");
            } else {
                System.out.println("Nessun piatto trovato con l'ID specificato.");
            }

        } catch (SQLException e) {
            System.out.println("Eccezione in updateDishInfo(MealDAO): " + e.getMessage());
        }
    }

    @Override
    public void update(Meal entity) {

    }

    @Override
    public void delete(int id) {

    }
}
