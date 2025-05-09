package model.transaction;

import model.account.Account;

public record DepositResult(model.transaction.DepositResult.Status status, Account account, int amount) {

    public enum Status {

        SUCCESS,
        ACCOUNT_NOT_FOUND
    }


    public Status getStatus() {

        return status;
    }

    public Account getAccount() {

        return account;
    }
}
