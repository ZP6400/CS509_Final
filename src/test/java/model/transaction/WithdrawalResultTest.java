package model.transaction;

import model.account.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithdrawalResultTest {

    @Test
    public void test_withdrawalResult_constructor_and_getters() {

        Account account = new Account(3, "Jane Does", 750, "Active");
        int withdrawal_amount = 500;
        WithdrawalResult.Status status = WithdrawalResult.Status.SUCCESS;

        WithdrawalResult result = new WithdrawalResult(status, account, withdrawal_amount);

        assertEquals(status, result.status());
        assertEquals(account, result.account());
        assertEquals(withdrawal_amount, result.amount());
    }
}

