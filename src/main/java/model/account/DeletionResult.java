package model.account;

public class DeletionResult {

    public enum Status {

        SUCCESS,
        CONFIRMATION_FAILURE,
    }

    private final DeletionResult.Status status;
    private final int account_num;

    public DeletionResult(DeletionResult.Status status, int account_number) {

        this.status = status;
        this.account_num = account_number;
    }

    public DeletionResult.Status getStatus() {

        return status;
    }

    public int getAccountNumber() {

        return account_num;
    }
}
