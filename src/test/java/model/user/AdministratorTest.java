package model.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdministratorTest {

    @Test
    public void test_administrator_constructor_and_getters() {

        String login = "admin_name";
        String pin = "13579";

        Administrator admin = new Administrator(login, pin);

        assertEquals(login, admin.getLogin());
        assertEquals(pin, admin.getPin());
    }
}

