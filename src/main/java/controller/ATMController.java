package controller;

import model.account.Account;
import model.account.AccountInfo;
import model.account.CreationResult;
import model.account.DeletionResult;
import model.transaction.DepositResult;
import model.transaction.WithdrawalResult;
import model.user.Administrator;
import model.user.Customer;
import model.user.User;
import repository.DatabaseManager;
import repository.exception.DatabaseException;
import service.AdminService;
import service.CustomerService;
import ui.ATMView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ATMController {

    //The controller acts as the bridge between the database/services  the interface. db_manager is simply used
    //to assist in logging in
    private final DatabaseManager db_manager;
    private final CustomerService customer_service;
    private final AdminService admin_service;
    private final ATMView view;


    public ATMController(DatabaseManager db_manager, CustomerService customer_service,
                         AdminService admin_service, ATMView view) {

        this.db_manager = db_manager;
        this.customer_service = customer_service;
        this.admin_service = admin_service;
        this.view = view;
    }


    public void start() throws DatabaseException {

        //Upon starting the program, the interface is told to display the welcome message
        view.displayMessage("Welcome to the ATM System!");

        User user = null;

        //While the user has yet to be determined
        while (user == null) {

            //The user is prompted for login credentials which are stored
            String login = view.promptLogin();
            String pin = view.promptPin();

            //The db_manager is then called to see if there is a user that matches the credentials
            user = db_manager.getUser(login, pin);

            if (user == null) {

                //If not, the interface prints the invalid login message
                view.displayMessage("Invalid login or pin code. Please try again.\n");
            }
        }

        //If the user is not null, it is checked to see if it is either a Customer or Administrator.
        //Depending on which of the two they are, they are sent to different menus
        if (user instanceof Customer) {

            view.displayMessage("\nLogin Successful! Welcome, Customer.");
            handleCustomerMenu((Customer) user);
        }
        else if (user instanceof Administrator) {

            view.displayMessage("\nLogin Successful! Welcome, Administrator.");
            handleAdminMenu();
        }
    }

    void handleCustomerMenu(Customer customer) throws DatabaseException {

        int choice;

        do {

            //The user is displayed the customer menu via the interface and prompted to make a selection
            view.displayCustomerMenu();
            choice = view.promptMenuChoice();

            switch (choice) {

                case 1:

                    //If the choice is 1, the user wants to make a withdrawal from their account
                    handleWithdrawal(customer);
                    break;

                case 2:

                    //If the choice is 2, the user wants to make a deposit to their account
                    handleDeposit(customer);
                    break;

                case 3:

                    //If the choice is 3, the user wants to view their balance information
                    handleBalanceInfo(customer);
                    break;

                case 4:

                    //If the choice is 4, the user is finished using the application
                    view.displayMessage("Thank you for using the ATM. Goodbye!");
                    break;

                default:

                    //Any other integer choice will be displayed as invalid
                    view.displayMessage("Invalid choice. Please try again.");
            }
        }
        //The loop continues so long as the choice is not equal to 4
        while (choice != 4);
    }

    void handleAdminMenu() throws DatabaseException {

        int choice;

        do {

            //The user is displayed the administrator menu via the interface and prompted to make a selection
            view.displayAdminMenu();
            choice = view.promptMenuChoice();

            switch (choice) {

                case 1:

                    //If the choice is 1, the user wants to create a new account
                    handleAccountCreation();
                    break;

                case 2:

                    //If the choice is 2, the user wants to delete an existing account
                    handleAccountDeletion();
                    break;

                case 3:

                    //If the choice is 3, the user wants to update an existing account
                    handleAccountUpdate();
                    break;

                case 4:

                    //If the choice is 4, the user wants to search for an existing account
                    handleAccountSearch();
                    break;

                case 5:

                    //If the choice is 5, the user is finished using the application
                    view.displayMessage("Thank you for using the ATM. Goodbye!");
                    break;

                default:

                    //Any other integer choice will be displayed as invalid
                    view.displayMessage("Invalid choice. Please try again.");
            }
        }
        //The loop continues so long as the choice is not equal to 5
        while (choice != 5);
    }


    void handleWithdrawal(Customer customer) throws DatabaseException {

        //The user is prompted to enter an amount of money to withdraw; this is stored
        int withdrawal_amount = view.promptWithdrawal();

        WithdrawalResult result = customer_service.withdrawCash(customer, withdrawal_amount);

        //There are three possible results from the withdrawal process
        switch (result.status()) {

            case ACCOUNT_NOT_FOUND:

                //The account is not found; this error is displayed via the interface
                view.displayError("Account not found.");
                break;

            case INSUFFICIENT_FUNDS:

                //There is not enough money available to withdraw; this is also displayed via the interface
                view.displayMessage("Withdrawal amount exceeds current balance. Please try again.");
                break;

            case SUCCESS:

                //The withdrawal is a success
                //The current date and time are acquired using LocalDateTime, and formatted to the proper pattern via
                //DateTimerFormatter
                LocalDateTime current_date = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String formatted_date = current_date.format(formatter);

                //The account information is acquired and printed to the terminal via the interface
                Account account = result.account();
                view.displayMessage("Cash Successfully Withdrawn.");
                view.displayMessage("Account #" + account.getAccountNumber());
                view.displayMessage("Date: " + formatted_date);
                view.displayMessage("Withdrawn: $" + result.amount());
                view.displayMessage("Balance: $" + account.getBalance());
                break;
        }
    }

    void handleDeposit(Customer customer) throws DatabaseException {

        //The user is prompted to enter an amount of money to deposit; this is stored
        int deposited_amount = view.promptDeposit();

        DepositResult result = customer_service.depositCash(customer, deposited_amount);

        //There are two possible results from the depositing process
        switch (result.status()) {

            case ACCOUNT_NOT_FOUND:

                //The account is not found; this error is displayed via the interface
                view.displayError("Account not found.");
                break;

            case SUCCESS:

                //The deposit is a success
                //The current date and time are acquired using LocalDateTime, and formatted to the proper pattern via
                //DateTimerFormatter
                LocalDateTime current_date = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String formatted_date = current_date.format(formatter);

                //The account information is acquired and printed to the terminal via the interface
                Account account = result.account();
                view.displayMessage("Cash Successfully Deposited.");
                view.displayMessage("Account #" + account.getAccountNumber());
                view.displayMessage("Date: " + formatted_date);
                view.displayMessage("Deposited: $" + result.amount());
                view.displayMessage("Balance: $" + account.getBalance());
                break;
        }
    }

    void handleBalanceInfo(Customer customer) {

        //The account information is acquired via the Customer class
        Account account = customer.getAccount();

        //If the account does exist:
        if (account != null) {

            //The current date and time are acquired using LocalDateTime, and formatted to the proper pattern via
            //DateTimerFormatter
            LocalDateTime current_date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String formatted_date = current_date.format(formatter);

            view.displayMessage("Account #" + account.getAccountNumber());
            view.displayMessage("Date: " + formatted_date);
            view.displayMessage("Balance: $" + account.getBalance());
        }
        else {

            //If the account doesn't exist, the error is printed via the interface
            view.displayError("Account not found.");
        }
    }

    void handleAccountCreation() throws DatabaseException {

        //The user is prompted to enter account information via the interface; all of this information
        //is stored in respective variables
        String login = view.promptLogin();
        String pin = view.promptPin();
        String holder_name = view.promptHolderName();
        int balance = view.promptStartingBalance();
        boolean is_active = view.promptAccountStatus();

        //Once all the information is acquired, createAccount() is called via the admin service
        CreationResult result = admin_service.createAccount(login, pin, holder_name, balance, is_active);

        //There are three possible results from trying to create an account
        switch (result.getStatus()) {

            case DUPLICATE_ACCOUNT:

                //The account name already exists; this is communicated to the user via the interface
                view.displayMessage("Account Creation Failed - Duplicate Entry.");
                break;

            case ERROR:

                //An error occurred, which is also communicated via the interface
                view.displayMessage("Account Creation Failed - An Error Occurred.");
                break;

            case SUCCESS:

                //The account is successfully made. The account number is acquired via CreationResult's
                //getAccountNumber()
                view.displayMessage("Account Successfully Created – the account number assigned is: "
                        + result.getAccountNumber());
                break;
        }
    }

    void handleAccountDeletion() throws DatabaseException {

        //The account number starts at -1 and stays that way until a valid account number is entered by the user
        int account_num = -1;
        while (account_num <= 0) {

            account_num = view.promptAccountNumberForDeletion();
            Account account = admin_service.getAccountIfExists(account_num);

            //If the account is null, then the number entered is not a valid number
            if (account == null && account_num > 0) {

                view.displayMessage("An Account with this account number does not exist.");

                //The account_num variable is reset back to -1
                account_num = -1;
            }
            else {

                //The name of the holder is acquired and used in the confirmation message displayed by the interface
                assert account != null;
                int confirmation = view.confirmDeletion(account.getHolderName());

                DeletionResult result = admin_service.deleteAccount(account.getAccountNumber(), confirmation);

                //There are two possible results from trying to delete an account
                switch (result.getStatus()) {

                    case CONFIRMATION_FAILURE:

                        //The confirmation value entered does not match the account number,
                        //so the deletion does not occur
                        view.displayMessage("Input does not match the account number. Deletion cancelled.");
                        break;

                    case SUCCESS:

                        //The deletion was a success; this is displayed to the user
                        view.displayMessage("Account Deleted Successfully.");
                        break;
                }
            }
        }
    }

    void handleAccountUpdate() throws DatabaseException {

        //The account number starts at -1 and stays that way until a valid account number is entered by the user
        int account_num = -1;
        while (account_num <= 0) {

            account_num = view.promptAccountNumber();
            Account account = admin_service.getAccountIfExists(account_num);

            //If the account is null, then the number entered is not a valid number
            if (account == null && account_num > 0) {

                view.displayMessage("An Account with this account number does not exist.");

                //The account_num variable is reset back to -1
                account_num = -1;
            }
        }

        //If the account is not null, then the necessary variables are prepared for updating an account
        String new_holder = "";
        String new_status = "";
        String new_login = "";
        String new_pin = "";

        boolean finished = false;

        //While the user is yet to be finished updating the selected account:
        while (!finished) {

            //The user is displayed with the features they can update via the interface and is then prompted
            //to make a selection
            view.displayUpdateChoice();
            int updateChoice = view.promptMenuChoice();
            switch (updateChoice) {
                case 1:

                    //If the option is 1, the holder's name is to be updated, and updateAccount() is then called
                    new_holder = view.promptNewHolderName();
                    view.displayMessage("Holder's name update registered.");
                    break;

                case 2:

                    //If the option is 2, the account's status is to be updated, and updateAccount() is then called
                    new_status = view.promptNewStatus();
                    view.displayMessage("Account status update registered.");
                    break;

                case 3:

                    //If the option is 3, the account's login is to be updated, and updateAccount() is then called
                    new_login = view.promptNewLogin();
                    view.displayMessage("Login update registered.");
                    break;

                case 4:

                    //If the option is 4, the account's pin code is to be updated
                    new_pin = view.promptNewPin();
                    view.displayMessage("Pin code update registered.");
                    break;

                case 5:

                    //If the option is 5, updateAccount() is called with all of the necessary parameters
                    boolean update_result = admin_service.updateAccount(
                            account_num, new_holder, new_status, new_login, new_pin);

                    //If the boolean returned is true, then the account was updated without issue
                    if (update_result) {

                        view.displayMessage("Account updated successfully.");
                    }
                    else {

                        //Otherwise, an error occurred; this is displayed via the interface
                        view.displayMessage("An error occurred. Please try again.");
                    }

                    //The update is finished, meaning the update menu can finally be exited
                    finished = true;
                    break;

                default:

                    //Any other integer choice will be displayed as invalid
                    view.displayMessage("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    void handleAccountSearch() throws DatabaseException {

        //The account number starts at -1 and stays that way until a valid account number is entered by the user
        int account_num = -1;
        while (account_num <= 0) {

            account_num = view.promptAccountNumber();
            Account account = admin_service.getAccountIfExists(account_num);

            //If the account is null, then the number entered is not a valid number
            if (account == null && account_num > 0) {

                view.displayMessage("An Account with this account number does not exist.");

                //The account_num variable is reset back to -1
                account_num = -1;
            }
            else if (account != null && account_num > 0) {

                //Otherwise, the account info is obtained via admin_service's searchAccount() function
                AccountInfo result = admin_service.searchAccount(account_num);

                //The user information is returned from result's getUser() function, and the interface is then
                //called to display both the account and user fields
                User user = result.user();
                view.showAccountInfo(account, user);
            }
        }
    }
}
