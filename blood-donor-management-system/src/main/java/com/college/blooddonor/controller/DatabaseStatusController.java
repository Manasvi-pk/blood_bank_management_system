package com.college.blooddonor.controller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseStatusController {

    private final DataSource dataSource;

    public DatabaseStatusController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/db-check")
    public ResponseEntity<DatabaseStatus> checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return ResponseEntity.ok(new DatabaseStatus("connected"));
            }
        } catch (SQLException ignored) {
            // Return a generic status only; never expose connection details or credentials.
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new DatabaseStatus("unavailable"));
    }

    private record DatabaseStatus(String status) {
    }
}
