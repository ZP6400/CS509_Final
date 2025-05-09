package repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseConnectionTest {

    @Test
    public void test_database_connection_constructor_and_getters() {

        String url = "jdbc:mysql://localhost:3306/test_db";
        String username = "admin";
        String password = "password";

        DatabaseConnection connection = new DatabaseConnection(url, username, password);

        assertEquals(url, connection.url());
        assertEquals(username, connection.username());
        assertEquals(password, connection.password());
    }
}

