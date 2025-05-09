package model.account;

public class Account {

    //The fields of the account class. The only field that is altered within the class
    //is balance, so the rest can remain as final
    private final int account_number;
    private final String holder_name;
    private int balance;
    private final String status;

    public Account(int account_number, String holder_name, int balance, String status) {

        this.account_number = account_number;
        this.holder_name = holder_name;
        this.balance = balance;
        this.status = status;
    }

    //The following are getters for each attribute in the class. This is unfortunately necessary for
    //one operation, but luckily no getters exist for the login and pin_code fields of the User class
    public int getaccount_number() {

        return account_number;
    }

    public String getHolderName() {

        return holder_name;
    }

    public int getBalance() {

        return balance;
    }

    public String getStatus() {

        return status;
    }


    public void deposit(int amount) {

        //As long as the amount being added is greater than 0:
        if (amount > 0) {

            //The balance of the account is updated
            this.balance += amount;
        }
    }

    public boolean withdraw(int amount) {

        //If the amount being withdrawn is greater than 0 and the current balance of
        //the account is greater than the amount being withdrawn:
        if (amount > 0 && balance >= amount) {

            //The balance is adjusted and 'true' is returned
            this.balance -= amount;
            return true;
        }

        //Otherwise, the balance is not changed and 'false' is returned
        return false;
    }
}
