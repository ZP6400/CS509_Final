package model.transaction;

import model.account.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositResultTest {

    @Test
    public void test_depositResult_constructor_and_getters() {

        Account account = new Account(2, "John Doe", 250, "Active");
        int deposited_amount = 1000;
        DepositResult.Status status = DepositResult.Status.SUCCESS;

        DepositResult result = new DepositResult(status, account, deposited_amount);

        assertEquals(status, result.status());
        assertEquals(account, result.account());
        assertEquals(deposited_amount, result.amount());
    }
}

