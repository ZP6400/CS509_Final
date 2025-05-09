package repository.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseExceptionTest {

    @Test
    public void test_database_exception_constructor() {

        String error_message = "Database connection error";
        Throwable cause = new Throwable("A connection timeout occurred");

        DatabaseException exception = new DatabaseException(error_message, cause);

        assertEquals(error_message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

