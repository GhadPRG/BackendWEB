package it.unical.web.backend.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//La classe DatabaseConnection è una classe utility, quindi non va istanziata, basta chiamare solo i metodi direttamente
//ad esempio, DatabaseConnection.getConnection();

public final class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/welliodb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    private DatabaseConnection(){}

    public static Connection getConnection(){

        Connection con=null;
        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to: "+con.getCatalog());
        }catch (SQLException e){
            System.out.println("Errore durante la connessione.");
            e.printStackTrace();
        }
        return con;
    }
}