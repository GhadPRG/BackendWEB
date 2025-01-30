package it.unical.web.backend;

import it.unical.web.backend.controller.DatabaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        DatabaseConnection.getConnection();

        SpringApplication.run(BackendApplication.class, args);
    }

}
