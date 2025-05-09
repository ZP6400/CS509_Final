package service;

import model.account.Account;
import model.transaction.DepositResult;
import model.transaction.WithdrawalResult;
import model.user.Customer;
import repository.DatabaseManager;
import repository.exception.DatabaseException;

public class CustomerService {

    //The DatabaseManager will be used to access the database of accounts in order to
    //make changes to certain columns
    private final DatabaseManager db_manager;

    public CustomerService(DatabaseManager db_manager) {

        this.db_manager = db_manager;
    }

    public WithdrawalResult withdrawCash(Customer customer, int amount) throws DatabaseException {

        //The account of the customer is retrieved via the getAccount() function from the Customer class
        Account account = customer.getAccount();

        //If the account is equal to null, this means it does not exist and therefore cash can not
        //be withdrawn from it
        if (account == null) {

            //A WithdrawalResult object with an ACCOUNT_NOT_FOUND status is created and returned
            return new WithdrawalResult(WithdrawalResult.Status.ACCOUNT_NOT_FOUND, null, amount);
        }

        //If the account exists (is not null) and the amount provided can safely be withdrawn:
        if (account.withdraw(amount)) {

            db_manager.updateAccountBalance(account.getaccount_number(), account.getBalance());

            //A WithdrawalResult object with a SUCCESS status is created and returned
            return new WithdrawalResult(WithdrawalResult.Status.SUCCESS, account, amount);
        }
        else {

            //If the amount provided cannot be safely withdrawn, a WithdrawalResult object with
            //status INSUFFICIENT_FUNDS is made and returned
            return new WithdrawalResult(WithdrawalResult.Status.INSUFFICIENT_FUNDS, account, amount);
        }
    }

    public DepositResult depositCash(Customer customer, int amount) throws DatabaseException {

        //The account of the customer is retrieved via the getAccount() function from the Customer class
        Account account = customer.getAccount();

        //If the account is not equal to null, meaning it exists:
        if (account != null) {

            //The deposit function is called to update the current state of the account. The updateAccountBalance
            //for db_manager is then called to ensure that the database is up to date
            account.deposit(amount);
            db_manager.updateAccountBalance(account.getaccount_number(), account.getBalance());

            //A WithdrawalResult object with a SUCCESS status is created and returned
            return new DepositResult(DepositResult.Status.SUCCESS, account, amount);
        }

        //A WithdrawalResult object with an ACCOUNT_NOT_FOUND status is created and returned
        return new DepositResult(DepositResult.Status.ACCOUNT_NOT_FOUND, null, amount);
    }
}