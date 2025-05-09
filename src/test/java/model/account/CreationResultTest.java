package model.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreationResultTest {

    @Test
    public void test_creationResult_constructor_and_getters() {

        CreationResult.Status status = CreationResult.Status.SUCCESS;
        int account_number = 1;

        CreationResult result = new CreationResult(status, account_number);

        assertEquals(status, result.getStatus());
        assertEquals(account_number, result.getAccountNumber());
    }
}

