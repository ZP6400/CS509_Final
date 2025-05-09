package model.user;

//Abstract base class for Users (Customers and Administrators extend from this)
public abstract class User {

    private final String login;
    private final String pin_code;

    public User(String login, String pin_code) {

        this.login = login;
        this.pin_code = pin_code;
    }

    public String getLogin() {

        return login;
    }

    public String getPin() {

        return pin_code;
    }
}

