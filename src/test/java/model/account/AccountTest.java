package model.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AccountTest {

    @Test
    public void test_account_constructor_and_getters() {

        Account account = new Account(1, "John Doe", 50000, "Active");

        assertEquals(1, account.getAccountNumber());
        assertEquals("John Doe", account.getHolderName());
        assertEquals(50000, account.getBalance());
        assertEquals("Active", account.getStatus());
    }


    @Test
    public void test_deposit_when_positive_amount_should_increases_balance() {

        Account account = new Account(1, "John Doe", 1000, "Active");

        //When deposit() is called, because a positive value is in the parameter, the balance should now be 1500
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    public void test_deposit_when_zero_or_negative_should_not_change_balance() {

        Account account = new Account(1, "Jane Doe", 1000, "Active");

        //When deposit() is called, because 0 is in the parameter, the balance should still be 1000
        account.deposit(0);
        assertEquals(1000, account.getBalance());

        //When deposit() is called, because a negative value is in the parameter, the balance should still be 1000
        account.deposit(-100);
        assertEquals(1000, account.getBalance());
    }

    @Test
    public void test_withdraw_when_valid_amount_provided_should_reduce_balance_and_returns_true() {

        Account account = new Account(1, "John Doe", 1000, "Active");
        boolean result = account.withdraw(500);

        //When withdraw() is called, because a value less than 1000 was provided, the balance should now be 500
        //and true should be returned
        assertTrue(result);
        assertEquals(500, account.getBalance());
    }

    @Test
    public void test_withdraw_when_amount_greater_than_balance_should_return_false() {

        Account account = new Account(1, "Jane Doe", 500, "Active");
        boolean result = account.withdraw(1000);

        //When withdraw() is called, because a value greater than 500 was provided, the balance should be the same
        //and false should be returned
        assertFalse(result);
        assertEquals(500, account.getBalance());
    }

    @Test
    public void test_withdraw_when_amount_zero_or_negative_should_return_false() {

        Account account = new Account(1, "John Doe", 1000, "Active");

        //When withdraw() is called with either 0 or a negative value, the balance should stay the same and false
        //should be returned for both calls
        assertFalse(account.withdraw(0));
        assertFalse(account.withdraw(-100));
        assertEquals(1000, account.getBalance());
    }


}
