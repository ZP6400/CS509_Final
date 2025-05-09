package controller;

import model.account.Account;
import model.account.AccountInfo;
import model.account.CreationResult;
import model.account.DeletionResult;
import model.transaction.DepositResult;
import model.transaction.WithdrawalResult;
import model.user.Administrator;
import model.user.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.DatabaseManager;
import repository.exception.DatabaseException;
import service.AdminService;
import service.CustomerService;
import ui.ATMView;

import static org.mockito.Mockito.*;

public class ATMControllerTest {

    private DatabaseManager db_manager_mock;
    private CustomerService customer_service_mock;
    private AdminService admin_service_mock;
    private ATMView view_mock;
    private ATMController controller;

    @BeforeEach
    public void setUp() {

        //Before every test is run, mocks are established to ensure only ATMController is tested
        db_manager_mock = mock(DatabaseManager.class);
        customer_service_mock = mock(CustomerService.class);
        admin_service_mock = mock(AdminService.class);
        view_mock = mock(ATMView.class);
        controller = new ATMController(db_manager_mock, customer_service_mock, admin_service_mock, view_mock);
    }


    @Test
    public void test_start_when_successful_customer_login_should_call_handleCustomerMenu() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Customer customer_mock = new Customer("Customer", "12345",
                new Account(1, "John Doe", 10000, "Active"));

        //When view_mock prompts for login and pin code, the following are provided
        when(view_mock.promptLogin()).thenReturn("Customer");
        when(view_mock.promptPin()).thenReturn("12345");

        //When db_manager_mock needs to get a user, the customer_mock is provided
        when(db_manager_mock.getUser("Customer", "12345")).thenReturn(customer_mock);

        //The following ensures that when handleCustomerMenu() is called, nothing actually happens, which
        //prevents the test getting trapped in a loop
        doNothing().when(controller_spy).handleCustomerMenu(customer_mock);

        controller_spy.start();

        //When start() is called with the Customer and customer information provided, the following should be instructed
        //to be printed by view_mock in succession
        verify(view_mock).displayMessage("Welcome to the ATM System!");
        verify(view_mock).displayMessage("\nLogin Successful! Welcome, Customer.");
    }

    @Test
    public void test_start_when_successful_admin_login_should_call_handleAdminMenu() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Administrator admin_mock = new Administrator("Admin", "56789");

        //When view_mock prompts for login and pin code, the following are provided
        when(view_mock.promptLogin()).thenReturn("Admin");
        when(view_mock.promptPin()).thenReturn("56789");

        //When db_manager_mock needs to get a user, the admin_mock is provided
        when(db_manager_mock.getUser("Admin", "56789")).thenReturn(admin_mock);

        //The following ensures that when handleAdminMenu() is called, nothing actually happens, which
        //prevents the test getting trapped in a loop
        doNothing().when(controller_spy).handleAdminMenu();

        controller_spy.start();

        //When start() is called with the Administrator and admin information provided, the following should be instructed
        //to be printed by view_mock in succession.
        verify(view_mock).displayMessage("Welcome to the ATM System!");
        verify(view_mock).displayMessage("\nLogin Successful! Welcome, Administrator.");
    }

    @Test
    public void test_start_when_invalid_login_should_retry_and_then_become_successful_customer_login() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Customer customer_mock = new Customer("Customer", "12345",
                new Account(2, "Jane Doe", 5000, "Active"));

        //When first logging in, Not_Customer is provided, then Customer. The same goes for the pin code;
        //invalid, then valid
        when(view_mock.promptLogin()).thenReturn("Not_Customer", "Customer");
        when(view_mock.promptPin()).thenReturn("00000", "12345");

        when(db_manager_mock.getUser("Not_Customer", "00000")).thenReturn(null);
        when(db_manager_mock.getUser("Customer", "12345")).thenReturn(customer_mock);

        //The following ensures that when handleCustomerMenu() is called, nothing actually happens, which
        //prevents the test getting trapped in a loop
        doNothing().when(controller_spy).handleCustomerMenu(customer_mock);

        controller_spy.start();

        //When start() is called with the invalid and then valid customer information provided, the following
        //should be instructed for view_mock to display to the terminal
        verify(view_mock).displayMessage("Welcome to the ATM System!");
        verify(view_mock).displayMessage("Invalid login or pin code. Please try again.\n");
        verify(view_mock).displayMessage("\nLogin Successful! Welcome, Customer.");
    }


    @Test
    public void test_handleCustomerMenu_when_withdraw_then_exit_should_call_handleWithdraw_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Customer customer_mock = new Customer("Customer", "12345",
                new Account(1, "John Doe", 5000, "Active"));

        //When prompted with a menu choice, 1 is first selected, then 4
        when(view_mock.promptMenuChoice()).thenReturn(1, 4);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayCustomerMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleWithdrawal(customer_mock);

        controller_spy.handleCustomerMenu(customer_mock);

        //When handleCustomerMenu() is called, the menu should be displayed twice, handleWithdrawal() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayCustomerMenu();
        verify(controller_spy).handleWithdrawal(customer_mock);
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleCustomerMenu_when_deposit_then_exit_should_call_handleDeposit_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Customer customer_mock = new Customer("Customer", "12345",
                new Account(2, "Jane Doe", 5000, "Active"));

        //When prompted with a menu choice, 2 is first selected, then 4
        when(view_mock.promptMenuChoice()).thenReturn(2, 4);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayCustomerMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleDeposit(customer_mock);

        controller_spy.handleCustomerMenu(customer_mock);

        //When handleCustomerMenu() is called, the menu should be displayed twice, handleDeposit() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayCustomerMenu();
        verify(controller_spy).handleDeposit(customer_mock);
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleCustomerMenu_when_view_balance_then_exit_should_call_handleCustomerMenu_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Customer customer_mock = new Customer("Customer", "12345",
                new Account(3, "John Doe", 5000, "Active"));

        //When prompted with a menu choice, 3 is first selected, then 4
        when(view_mock.promptMenuChoice()).thenReturn(3, 4);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayCustomerMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleBalanceInfo(customer_mock);

        controller_spy.handleCustomerMenu(customer_mock);

        //When handleCustomerMenu() is called, the menu should be displayed twice, handleBalanceInfo() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayCustomerMenu();
        verify(controller_spy).handleBalanceInfo(customer_mock);
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleCustomerMenu_when_invalid_input_then_exit_should_print_invalid_choice_and_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        Customer customer_mock = new Customer("Customer", "12345",
                new Account(4, "Jane Doe", 5000, "Active"));

        //When prompted with a menu choice, an invalid integer is first selected, then 4
        when(view_mock.promptMenuChoice()).thenReturn(99, 4);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayCustomerMenu();
        doNothing().when(view_mock).displayMessage(anyString());

        controller_spy.handleCustomerMenu(customer_mock);

        //When handleCustomerMenu() is called, the menu should be displayed twice, and view_mock should be
        //instructed to display the two messages below
        verify(view_mock, times(2)).displayCustomerMenu();
        verify(view_mock).displayMessage("Invalid choice. Please try again.");
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }


    @Test
    public void test_handleAdminMenu_when_create_account_then_exit_should_call_handleAccountCreation_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        //When prompted with a menu choice, 1 is first selected, then 5
        when(view_mock.promptMenuChoice()).thenReturn(1, 5);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayAdminMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleAccountCreation();

        controller_spy.handleAdminMenu();

        //When handleAdminMenu() is called, the menu should be displayed twice, handleAccountCreation() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayAdminMenu();
        verify(controller_spy).handleAccountCreation();
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleAdminMenu_when_delete_account_then_exit_should_call_handleAccountDeletion_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        //When prompted with a menu choice, 2 is first selected, then 5
        when(view_mock.promptMenuChoice()).thenReturn(2, 5);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayAdminMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleAccountDeletion();

        controller_spy.handleAdminMenu();

        //When handleAdminMenu() is called, the menu should be displayed twice, handleAccountDeletion() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayAdminMenu();
        verify(controller_spy).handleAccountDeletion();
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleAdminMenu_when_update_account_then_exit_should_call_handleAccountUpdate_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        //When prompted with a menu choice, 3 is first selected, then 5
        when(view_mock.promptMenuChoice()).thenReturn(3, 5);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayAdminMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleAccountUpdate();

        controller_spy.handleAdminMenu();

        //When handleAdminMenu() is called, the menu should be displayed twice, handleAccountUpdate() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayAdminMenu();
        verify(controller_spy).handleAccountUpdate();
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleAdminMenu_when_search_account_then_exit_should_call_handleAccountSearch_and_print_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = Mockito.spy(controller);

        //When prompted with a menu choice, 4 is first selected, then 5
        when(view_mock.promptMenuChoice()).thenReturn(4, 5);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayAdminMenu();
        doNothing().when(view_mock).displayMessage(anyString());
        doNothing().when(controller_spy).handleAccountSearch();

        controller_spy.handleAdminMenu();

        //When handleAdminMenu() is called, the menu should be displayed twice, handleAccountSearch() should be
        //called, and view_mock should be instructed to print the exit message
        verify(view_mock, times(2)).displayAdminMenu();
        verify(controller_spy).handleAccountSearch();
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }

    @Test
    public void test_handleAdminMenu_when_invalid_input_then_exit_should_print_invalid_choice_and_exit_message() throws DatabaseException {

        //For this test, a controller_spy of ATMController is needed to be made
        ATMController controller_spy = spy(controller);

        //When prompted with a menu choice, an invalid integer is first selected, then 5
        when(view_mock.promptMenuChoice()).thenReturn(99, 5);

        //The following ensures that the program does not get caught in a loop or stuck
        doNothing().when(view_mock).displayAdminMenu();
        doNothing().when(view_mock).displayMessage(anyString());

        controller_spy.handleAdminMenu();

        //When handleAdminMenu() is called, the menu should be displayed twice, and view_mock should be
        //instructed to display the two messages below
        verify(view_mock, times(2)).displayAdminMenu();
        verify(view_mock).displayMessage("Invalid choice. Please try again.");
        verify(view_mock).displayMessage("Thank you for using the ATM. Goodbye!");
    }








    @Test
    public void test_handleWithdrawal_when_success_should_notify_user_and_display_info() throws DatabaseException {

        //Mocks of Customer and Account are initialized; account_mock is specifically initialized in its post-withdrawal state
        Customer customer_mock = mock(Customer.class);
        Account account_mock = new Account(1, "John Doe", 800, "Active");

        //A new WithdrawalResult is initialized with the SUCCESS status
        WithdrawalResult result = new WithdrawalResult(WithdrawalResult.Status.SUCCESS, account_mock, 200);

        when(view_mock.promptWithdrawal()).thenReturn(200);
        when(customer_service_mock.withdrawCash(customer_mock, 200)).thenReturn(result);

        controller.handleWithdrawal(customer_mock);

        //When handleWithdrawal() is called with the specific customer_mock, SUCCESS should occur, and the following
        //messages should be displayed by the view_mock
        verify(view_mock).displayMessage("Cash Successfully Withdrawn.");
        verify(view_mock).displayMessage("Account #1");
        verify(view_mock).displayMessage(matches("Date: \\d{2}/\\d{2}/\\d{4}"));  //(Date should be in MM/dd/yyyy format)
        verify(view_mock).displayMessage("Withdrawn: $200");
        verify(view_mock).displayMessage("Balance: $800");
    }

    @Test
    public void test_handleWithdrawal_when_insufficient_funds_should_prevent_withdrawal() throws DatabaseException {

        //A mock of Customer is initialized, and a new WithdrawalResult is initialized with the INSUFFICIENT_FUNDS status
        Customer customer_mock = mock(Customer.class);
        WithdrawalResult result = new WithdrawalResult(WithdrawalResult.Status.INSUFFICIENT_FUNDS, null, 200);

        when(view_mock.promptWithdrawal()).thenReturn(200);
        when(customer_service_mock.withdrawCash(customer_mock, 200)).thenReturn(result);

        controller.handleWithdrawal(customer_mock);

        //When handleWithdrawal() is called with the specific customer_mock, INSUFFICIENT_FUNDS should occur, and the following
        //message should be displayed by the view_mock
        verify(view_mock).displayMessage("Withdrawal amount exceeds current balance. Please try again.");
    }

    @Test
    public void test_handleWithdrawal_when_account_not_found_should_display_error() throws DatabaseException {

        //A mock of Customer is initialized, and a new WithdrawalResult is initialized with the ACCOUNT_NOT_FOUND status
        Customer mockCustomer = mock(Customer.class);
        WithdrawalResult result = new WithdrawalResult(WithdrawalResult.Status.ACCOUNT_NOT_FOUND, null, 0);

        when(view_mock.promptWithdrawal()).thenReturn(100);
        when(customer_service_mock.withdrawCash(mockCustomer, 100)).thenReturn(result);

        controller.handleWithdrawal(mockCustomer);

        //When handleWithdrawal() is called with the specific customer_mock, ACCOUNT_NOT_FOUND should occur, and the following
        //error should be displayed by the view_mock
        verify(view_mock).displayError("Account not found.");
    }


    @Test
    public void test_handleDeposit_when_success_should_notify_user_and_display_info() throws DatabaseException {

        //Mocks of Customer and Account are initialized; account_mock is specifically initialized in its post-deposit state
        Customer customer_mock = mock(Customer.class);
        Account account_mock = new Account(2, "Jane Doe", 1200, "Active");

        //A new DepositResult is initialized with the SUCCESS status
        DepositResult result = new DepositResult(DepositResult.Status.SUCCESS, account_mock, 200);

        when(view_mock.promptDeposit()).thenReturn(200);
        when(customer_service_mock.depositCash(customer_mock, 200)).thenReturn(result);

        controller.handleDeposit(customer_mock);

        //When handleDeposit() is called with the specific customer_mock, SUCCESS should occur, and the following
        //messages should be displayed by the view_mock
        verify(view_mock).displayMessage("Cash Successfully Deposited.");
        verify(view_mock).displayMessage("Account #2");
        verify(view_mock).displayMessage(matches("Date: \\d{2}/\\d{2}/\\d{4}"));
        verify(view_mock).displayMessage("Deposited: $200");
        verify(view_mock).displayMessage("Balance: $1200");
    }

    @Test
    public void test_handleDeposit_when_account_not_found_should_display_error() throws DatabaseException {

        //A mock of Customer is initialized, and a new DepositResult is initialized with the ACCOUNT_NOT_FOUND status
        Customer customer_mock = mock(Customer.class);
        DepositResult result = new DepositResult(DepositResult.Status.ACCOUNT_NOT_FOUND, null, 0);

        when(view_mock.promptDeposit()).thenReturn(100);
        when(customer_service_mock.depositCash(customer_mock, 100)).thenReturn(result);

        controller.handleDeposit(customer_mock);

        //When handleDeposit() is called with the specific customer_mock, ACCOUNT_NOT_FOUND should occur, and the following
        //error should be displayed by the view_mock
        verify(view_mock).displayError("Account not found.");
    }


    @Test
    public void test_handleBalanceInfo_when_account_exists_should_display_account_info_and_balance() {

        //Mocks of Customer and Account are initialized
        Customer customer_mock = mock(Customer.class);
        Account account_mock = new Account(17, "John Doe", 20000, "Active");

        when(customer_mock.getAccount()).thenReturn(account_mock);

        controller.handleBalanceInfo(customer_mock);

        //When handleDeposit() is called with the specific customer_mock, the following should be called to get displayed
        //by the view_mock
        verify(view_mock).displayMessage("Account #17");
        verify(view_mock).displayMessage(matches("Date: \\d{2}/\\d{2}/\\d{4}"));
        verify(view_mock).displayMessage("Balance: $20000");
    }

    @Test
    public void test_handleBalanceInfo_when_account_is_nonexistent_should_display_error() {

        //A mock of Customer that returns a null account is initialized
        Customer customer_mock = mock(Customer.class);
        when(customer_mock.getAccount()).thenReturn(null);

        controller.handleBalanceInfo(customer_mock);

        //When handleDeposit() is called with the specific customer_mock, view_mock should display the error that no
        //account was found
        verify(view_mock).displayError("Account not found.");
    }

    @Test
    public void test_handleAccountCreation_when_account_creation_is_successful_should_display_account_created_message() throws DatabaseException {

        //When view_mock prompts the necessary information for creating an account, valid information is to be provided
        when(view_mock.promptLogin()).thenReturn("ZP6400");
        when(view_mock.promptPin()).thenReturn("12345");
        when(view_mock.promptHolderName()).thenReturn("Zack Perry");
        when(view_mock.promptStartingBalance()).thenReturn(500);
        when(view_mock.promptAccountStatus()).thenReturn(true);

        //A new CreationResult is initialized with the SUCCESS status and account #1
        CreationResult result = new CreationResult(CreationResult.Status.SUCCESS, 1);

        when(admin_service_mock.createAccount(anyString(), anyString(), anyString(), anyInt(), anyBoolean())).thenReturn(result);

        controller.handleAccountCreation();

        //When handleAccountCreation() is called with new and valid account information, view_mock should be called
        //to display a success message that includes the number of the account
        verify(view_mock).displayMessage("Account Successfully Created – the account number assigned is: 1");
    }

    @Test
    public void test_handleAccountCreation_when_duplicate_account_should_display_duplicate_message() throws DatabaseException {

        //When view_mock prompts the necessary information for creating an account, valid information is to be provided
        when(view_mock.promptLogin()).thenReturn("ZP6400");
        when(view_mock.promptPin()).thenReturn("12345");
        when(view_mock.promptHolderName()).thenReturn("Zack Perry");
        when(view_mock.promptStartingBalance()).thenReturn(500);
        when(view_mock.promptAccountStatus()).thenReturn(true);

        //A new CreationResult is initialized with the DUPLICATE_ACCOUNT status
        CreationResult result = new CreationResult(CreationResult.Status.DUPLICATE_ACCOUNT, -1);

        when(admin_service_mock.createAccount(anyString(), anyString(), anyString(), anyInt(), anyBoolean())).thenReturn(result);

        controller.handleAccountCreation();

        //When handleAccountCreation() is called with valid but already existing account information, view_mock should be called
        //to display an account creation failure message
        verify(view_mock).displayMessage("Account Creation Failed - Duplicate Entry.");
    }

    @Test
    public void test_handleAccountCreation_when_error_occurs_should_display_error_message() throws DatabaseException {

        //When view_mock prompts the necessary information for creating an account, valid information is to be provided
        when(view_mock.promptLogin()).thenReturn("ZP6400");
        when(view_mock.promptPin()).thenReturn("12345");
        when(view_mock.promptHolderName()).thenReturn("Zack Perry");
        when(view_mock.promptStartingBalance()).thenReturn(500);
        when(view_mock.promptAccountStatus()).thenReturn(true);

        //A new CreationResult is initialized with the ERROR status
        CreationResult result = new CreationResult(CreationResult.Status.ERROR, -1);
        when(admin_service_mock.createAccount(anyString(), anyString(), anyString(), anyInt(), anyBoolean())).thenReturn(result);

        controller.handleAccountCreation();

        //When handleAccountCreation() is called with valid information but an error occurs, view_mock should be called
        //to display an account creation failure message
        verify(view_mock).displayMessage("Account Creation Failed - An Error Occurred.");
    }


    @Test
    public void test_handleAccountDeletion_when_confirmation_is_correct_should_display_success_message() throws DatabaseException {

        int account_num = 17;
        Account account_mock = new Account(account_num, "Jane Doe", 50000, "Active");

        //When view_mock prompts for an account number, account_num is provided. When admin_service_mock prompts for an account,
        //account_mock is returned
        when(view_mock.promptAccountNumberForDeletion()).thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);

        when(view_mock.confirmDeletion("Jane Doe")).thenReturn(account_num);

        //A new DeletionResult is initialized with the SUCCESS status
        DeletionResult result = new DeletionResult(DeletionResult.Status.SUCCESS, account_num);
        when(admin_service_mock.deleteAccount(account_num, account_num)).thenReturn(result);

        controller.handleAccountDeletion();

        //When handleAccountDeletion() is called with an existing account number and successful confirmation, the
        //view_mock should be instructed to display the success message
        verify(view_mock).displayMessage("Account Deleted Successfully.");
    }

    @Test
    public void test_handleAccountDeletion_when_account_does_not_exist_should_display_error_and_repeat() throws DatabaseException {

        int account_num = 17;
        int nonexistent_account_num = 42;

        //In the first instance promptAccountNumberForDeletion() is called, the nonexistent account number is returned first.
        //Then, the actual account number is provided
        when(view_mock.promptAccountNumberForDeletion()).thenReturn(nonexistent_account_num, account_num);

        //First getAccountIfExists() call returns null; second call returns account_mock
        when(admin_service_mock.getAccountIfExists(nonexistent_account_num)).thenReturn(null);
        Account account_mock = new Account(account_num, "Jane Doe", 500, "Active");
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);

        when(view_mock.confirmDeletion("Jane Doe")).thenReturn(account_num);

        //A new DeletionResult is initialized with the SUCCESS status
        DeletionResult result = new DeletionResult(DeletionResult.Status.SUCCESS, account_num);
        when(admin_service_mock.deleteAccount(account_num, account_num)).thenReturn(result);

        controller.handleAccountDeletion();

        //When handleAccountDeletion() is called with a nonexistent account number, the view_mock should be instructed
        //to display an error message. Then, when an existing account number is provided and successful confirmation
        //is performed, view_mock should be instructed to display the success message
        verify(view_mock).displayMessage("An Account with this account number does not exist.");
        verify(view_mock).displayMessage("Account Deleted Successfully.");
    }

    @Test
    public void test_handleAccountDeletion_when_confirmation_fails_should_display_deletion_cancelled_message() throws DatabaseException {

        int account_num = 17;
        int incorrect_confirmation = 12;
        Account mockAccount = new Account(account_num, "John Doe", 300, "Active");

        //When view_mock prompts for an account number, account_num is provided. When admin_service_mock prompts for an account,
        //account_mock is returned
        when(view_mock.promptAccountNumberForDeletion()).thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(mockAccount);

        //When confirmDeletion() is called, the incorrect number is provided
        when(view_mock.confirmDeletion("John Doe")).thenReturn(incorrect_confirmation);

        //A new DeletionResult is initialized with the CONFIRMATION_FAILURE status
        DeletionResult result = new DeletionResult(DeletionResult.Status.CONFIRMATION_FAILURE, account_num);
        when(admin_service_mock.deleteAccount(account_num, incorrect_confirmation)).thenReturn(result);

        controller.handleAccountDeletion();

        //When handleAccountDeletion() is called with an existing account number but incorrect confirmation, the
        //view_mock should be instructed to display the deletion cancellation message
        verify(view_mock).displayMessage("Input does not match the account number. Deletion cancelled.");
    }


    @Test
    public void test_handleAccountUpdate_when_all_fields_updated_successfully_should_display_success_messages() throws DatabaseException {

        int account_num = 17;
        Account account_mock = new Account(account_num, "Jane Doe", 1500, "Active");

        //When the interface prompts the user for an account number, account_num is provided. When the admin service prompts
        //for an account, account_mock is provided
        when(view_mock.promptAccountNumber()).thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);

        //When the update menu prompts for a selection: 1 is chosen, then 2, then 3, then 4, and finally 5
        when(view_mock.promptMenuChoice()).thenReturn(1, 2, 3, 4, 5);

        //The following strings are provided at each of the following prompts as well
        when(view_mock.promptNewHolderName()).thenReturn("Jane Smith");
        when(view_mock.promptNewStatus()).thenReturn("Disabled");
        when(view_mock.promptNewLogin()).thenReturn("JS6400");
        when(view_mock.promptNewPin()).thenReturn("67890");

        when(admin_service_mock.updateAccount(account_num, "Jane Smith", "Disabled", "JS6400", "67890"))
                .thenReturn(true);

        controller.handleAccountUpdate();

        //When handleAccountUpdate() is called with an existing account number and goes through all fields, altering each
        //aspect of the account, view_mock should be called to display the following
        verify(view_mock).displayMessage("Holder's name update registered.");
        verify(view_mock).displayMessage("Account status update registered.");
        verify(view_mock).displayMessage("Login update registered.");
        verify(view_mock).displayMessage("Pin code update registered.");
        verify(view_mock).displayMessage("Account updated successfully.");
    }

    @Test
    public void test_handleAccountUpdate_when_account_not_found_should_retry_prompting() throws DatabaseException {

        int account_num = 17;
        int nonexistent_account_num = 42;

        //In the first instance promptAccountNumber() is called, the nonexistent account number is returned first.
        //Then, the actual account number is provided
        when(view_mock.promptAccountNumber()).thenReturn(nonexistent_account_num, account_num);

        //First getAccountIfExists() call returns null; second call returns an actual account
        when(admin_service_mock.getAccountIfExists(nonexistent_account_num)).thenReturn(null);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(
                new Account(account_num, "John Doe", 0, "Active"));

        //The update loop is immediately ended upon it first being entered
        when(view_mock.promptMenuChoice()).thenReturn(5);
        when(admin_service_mock.updateAccount(eq(account_num), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        controller.handleAccountUpdate();

        //When handleAccountUpdate() is called with a nonexistent account number first, view_mock should be called to
        //display a message notifying the user
        verify(view_mock).displayMessage("An Account with this account number does not exist.");
        verify(view_mock).displayMessage("Account updated successfully.");
    }

    @Test
    public void test_handleAccountUpdate_when_update_fails_should_display_error_message() throws DatabaseException {

        int account_num = 10;
        Account account_mock = new Account(account_num, "Jane Doe", 100, "Active");

        //When the interface prompts the user for an account number, account_num is provided. When the admin service prompts
        //for an account, account_mock is provided
        when(view_mock.promptAccountNumber()).thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);

        when(view_mock.promptMenuChoice()).thenReturn(5);

        when(admin_service_mock.updateAccount(anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(false);

        controller.handleAccountUpdate();

        //When handleAccountUpdate() is called with an existing account number and the update menu is passed through
        //but an error occurs, the view_mock should be instructed to display a message to the user
        verify(view_mock).displayMessage("An error occurred. Please try again.");
    }

    @Test
    public void test_handleAccountUpdate_when_invalid_choice_should_display_error_message() throws DatabaseException {

        int account_num = 30;
        Account account_mock = new Account(account_num, "John Doe", 45000, "Active");

        //When the interface prompts the user for an account number, account_num is provided. When the admin service prompts
        //for an account, account_mock is provided
        when(view_mock.promptAccountNumber()).thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);

        //When accessing the menu, an invalid integer is entered first, then the exit integer
        when(view_mock.promptMenuChoice()).thenReturn(99, 5);

        when(admin_service_mock.updateAccount(anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        controller.handleAccountUpdate();

        //When handleAccountUpdate() is called with an existing account number and the update menu is accessed with
        //initial failure and then exit success, the view_mock should be called to print the following messages
        verify(view_mock).displayMessage("Invalid choice. Please try again.");
        verify(view_mock).displayMessage("Account updated successfully.");
    }


    @Test
    public void test_handleAccountSearch_when_search_successful_should_display_account_info() throws DatabaseException {

        int account_num = 17;
        Account account_mock = new Account(account_num, "Jane Doe", 75000, "Active");
        Customer user_mock = new Customer("JD6500", "12345", account_mock);

        //A new AccountInfo object is initialized with the information provided by account_mock and user_mock
        AccountInfo info = new AccountInfo(account_mock, user_mock);

        when(view_mock.promptAccountNumber()).thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);
        when(admin_service_mock.searchAccount(account_num)).thenReturn(info);

        controller.handleAccountSearch();

        //When handleAccountSearch() is called with valid account and user information, view_mock should be
        //instructed to call showAccountInfo()
        verify(view_mock).showAccountInfo(account_mock, user_mock);
    }

    @Test
    public void test_handleAccountSearch_when_nonexistent_account_number_provided_should_prompt_again() throws DatabaseException {

        int account_num = 50;
        int nonexistent_account_num = 100;
        Account account_mock = new Account(account_num, "John Doe", 10, "Active");
        Customer user_mock = new Customer("JD6600", "56789", account_mock);

        //A new AccountInfo object is initialized with the information provided by account_mock and user_mock
        AccountInfo info = new AccountInfo(account_mock, user_mock);

        //When view_mock prompts the user with an account number, the nonexistent number is provided first, then the
        //actual account number
        when(view_mock.promptAccountNumber())
                .thenReturn(nonexistent_account_num)
                .thenReturn(account_num);
        when(admin_service_mock.getAccountIfExists(nonexistent_account_num)).thenReturn(null);
        when(admin_service_mock.getAccountIfExists(account_num)).thenReturn(account_mock);
        when(admin_service_mock.searchAccount(account_num)).thenReturn(info);

        controller.handleAccountSearch();

        //When handleAccountSearch() is called with valid account and user information, view_mock should be
        //instructed to call showAccountInfo()
        verify(view_mock).displayMessage("An Account with this account number does not exist.");
        verify(view_mock).showAccountInfo(account_mock, user_mock);
    }
}

