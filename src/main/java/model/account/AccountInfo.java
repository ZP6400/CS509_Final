package model.account;

import model.user.User;

public record AccountInfo(Account account, User user) {

    public Account getAccount() {

        return account;
    }

    public User getUser() {

        return user;
    }
}
