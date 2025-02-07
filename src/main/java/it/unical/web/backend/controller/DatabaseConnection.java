package it.unical.web.backend.controller;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//La classe DatabaseConnection è una classe utility, quindi non va istanziata, basta chiamare solo i metodi direttamente
//ad esempio, DatabaseConnection.getConnection();

@Getter
public final class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/welliodb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";


    // Variabile statica che contiene l'unica istanza della classe
    private static DatabaseConnection instance;

    // Metodo per ottenere la connessione al database
    // Connessione al database
    private Connection connection;

    // Costruttore privato per impedire l'istanziazione diretta
    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to: " + connection.getCatalog());
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione: " + e.getMessage());
        }
    }

    // Metodo statico per ottenere l'unica istanza della classe
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

}