package model.account;

public class CreationResult {

    public enum Status {

        SUCCESS,
        DUPLICATE_ACCOUNT,
        ERROR
    }

    private final CreationResult.Status status;
    private final int account_number;

    public CreationResult(CreationResult.Status status, int account_number) {

        this.status = status;
        this.account_number = account_number;
    }

    public CreationResult.Status getStatus() {

        return status;
    }

    public int getaccount_number() {

        return account_number;
    }
}
