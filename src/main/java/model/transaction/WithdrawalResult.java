package model.transaction;

import model.account.Account;

public record WithdrawalResult(model.transaction.WithdrawalResult.Status status, Account account, int amount) {

    public enum Status {

        SUCCESS,
        ACCOUNT_NOT_FOUND,
        INSUFFICIENT_FUNDS
    }


    public Status getStatus() {

        return status;
    }

    public Account getAccount() {

        return account;
    }
}
