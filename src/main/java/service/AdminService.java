package service;

import model.account.AccountInfo;
import model.account.CreationResult;
import model.account.DeletionResult;
import model.user.User;
import model.account.Account;
import repository.DatabaseManager;
import repository.exception.DatabaseException;

public class AdminService {

    //The DatabaseManager object will be used to access the database of accounts in order to
    //make changes to certain rows or columns
    private final DatabaseManager db_manager;

    public AdminService(DatabaseManager db_manager) {

        this.db_manager = db_manager;
    }


    public Account getAccountIfExists(int account_number) throws DatabaseException {

        return db_manager.getAccount(account_number);
    }


    public CreationResult createAccount(String login, String pin, String holder,
                                        int starting_balance, boolean is_active) throws DatabaseException {

        //Initialize the necessary status string variable
        String status;

        //If the boolean passed through is true:
        if (is_active) {

            //The string is adjusted to "Active"
            status = "Active";
        }
        else {

            //Otherwise, the string is set to "Disabled"
            status = "Disabled";
        }

        //The following parameters are now passed through to the DatabaseManager's createNewAccount function.
        //The result is returned and stored in the account_num int
        int account_num = db_manager.createNewAccount(login, pin, holder, starting_balance, status);

        //If the account number is equal to -2:
        if (account_num == -2) {

            //A duplicate entry was trying to be made. The respective CreationResult object is made and returned
            return new CreationResult(CreationResult.Status.DUPLICATE_ACCOUNT, -1);
        }
        //If the account number is equal to -1:
        else if (account_num == -1) {

            //A different kind of error occurred; the respective CreationResult object is made and returned
            return new CreationResult(CreationResult.Status.ERROR, -1);
        }
        else {

            //A CreationResult object is made and returned letting the user know the account creation was successful
            return new CreationResult(CreationResult.Status.SUCCESS, account_num);
        }
    }

    public DeletionResult deleteAccount(int account_num, int confirmation_input) throws DatabaseException {

        //If the confirmation_input is not equal to the account number:
        if (confirmation_input != account_num) {

            //The deletion of the account was a failure; the respective DeletionResult object is made and returned
            return new DeletionResult(DeletionResult.Status.CONFIRMATION_FAILURE, account_num);
        }

        //Otherwise, the db_manager is called to delete the account. Then, a DeletionResult object is made and
        //returned to notify the user of the deletion success
        db_manager.deleteAccount(account_num);
        return new DeletionResult(DeletionResult.Status.SUCCESS, account_num);
    }

    public boolean updateAccount(int account_num, String new_holder,
                                 String new_status, String new_login, String new_pin) throws DatabaseException {

        //The updateAccountInfo() function is called, with the same parameters provided to updateAccount()
        //provided to this function
        return db_manager.updateAccountInfo(account_num, new_holder, new_status, new_login, new_pin);
    }

    public AccountInfo searchAccount(int account_num) throws DatabaseException {

        //Get a hold of the account that is being searched, based on the account number that was provided
        Account account = db_manager.getAccount(account_num);

        //If the account is not equal to null, meaning it exists:
        if (account != null) {

            //The user is acquired via the database and is used alongside the account to return
            //an initialized AccountInfo object
            User user = db_manager.getUser(account_num);
            return new AccountInfo(account, user);
        }
        else {

            //If the account is equal to null, that means the account with the account number provided by the
            //user is not in the database. Null is returned
            return null;
        }
    }
}
