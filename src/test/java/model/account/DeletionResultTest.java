package model.account;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletionResultTest {

    @Test
    public void test_deletionResult_constructor_and_getter() {

        DeletionResult.Status status = DeletionResult.Status.SUCCESS;

        //The account_number can be ignored in this instance
        DeletionResult result = new DeletionResult(status, 17);

        assertEquals(status, result.getStatus());
    }
}

