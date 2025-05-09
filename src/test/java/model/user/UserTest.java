package model.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    //Since User is abstract, a concrete subclass is needed for testing
    private static class TestUser extends User {

        public TestUser(String login, String pin_code) {

            super(login, pin_code);
        }
    }

    @Test
    public void test_user_constructor_and_getters() {

        String login = "user_name";
        String pin = "12345";

        User user = new TestUser(login, pin);

        assertEquals(login, user.getLogin());
        assertEquals(pin, user.getPin());
    }
}
