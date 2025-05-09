package model.user;

import model.account.Account;

//The Customer class, which extends the User class
public class Customer extends User {

    //Each customer has an account field
    private final Account account;

    //The constructor for the Customer class, which includes the
    //addition of an Account parameter
    public Customer(String login, String pin_code, Account account) {

        super(login, pin_code);
        this.account = account;
    }

    //A getter function, which returns the account associated with the specific Customer
    public Account getAccount() {

        return account;
    }
}
