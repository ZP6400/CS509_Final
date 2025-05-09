package model.user;

import model.account.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest {

    @Test
    public void test_customer_constructor_and_getters() {

        String login = "customer_name";
        String pin = "24680";
        Account account = new Account(1, "John Doe", 1000, "Active");

        Customer customer = new Customer(login, pin, account);

        assertEquals(login, customer.getLogin());
        assertEquals(pin, customer.getPin());
        assertEquals(account, customer.getAccount());
    }
}

