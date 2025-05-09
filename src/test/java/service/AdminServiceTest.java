package service;

import model.account.Account;
import model.account.AccountInfo;
import model.account.CreationResult;
import model.account.DeletionResult;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseManager;
import repository.exception.DatabaseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    private DatabaseManager db_manager_mock;
    private AdminService admin_service;

    @BeforeEach
    public void setUp() {

        db_manager_mock = mock(DatabaseManager.class);

        admin_service = new AdminService(db_manager_mock);
    }


    @Test
    public void test_getAccountIfExists_when_account_exists_should_return_account() throws DatabaseException {

        int account_num = 1;
        Account account_mock = new Account(account_num, "John Doe", 1250, "Active");

        //When db_manager_mock calls getAccount(), the mock account is to be returned
        when(db_manager_mock.getAccount(account_num)).thenReturn(account_mock);

        Account account = admin_service.getAccountIfExists(account_num);

        //When getAccountIfExists() is called when account exists, the account returned should not be null and should
        //also have an account number that matches account_num
        assertNotNull(account);
        assertEquals(account_num, account.getAccountNumber());
    }


    @Test
    void test_createAccount_when_account_successfully_made_should_return_success_CreationResult() throws DatabaseException {

        String login = "JD5700";
        String pin = "12345";
        String holder = "John Doe";
        int starting_balance = 5550;
        boolean is_active = true;

        //When createNewAccount() called, an account number is to be returned
        when(db_manager_mock.createNewAccount(login, pin, holder, starting_balance, "Active")).thenReturn(1);

        CreationResult result = admin_service.createAccount(login, pin, holder, starting_balance, is_active);

        //When createAccount() is called and runs successfully, a CreationResult object should be returned with
        //a SUCCESS status; account numbers should also match, and createNewAccount() should have been called once
        assertEquals(CreationResult.Status.SUCCESS, result.getStatus());
        assertEquals(1, result.getAccountNumber());
        verify(db_manager_mock, times(1)).createNewAccount(login, pin, holder, starting_balance, "Active");
    }

    @Test
    void test_createAccount_when_account_already_exists_should_return_duplicate_account_CreationResult() throws DatabaseException {

        String login = "existing_user";
        String pin = "54321";
        String holder = "Jane Doe";
        int starting_balance = 500;
        boolean is_active = false;

        //When createNewAccount() called, -2 is to be returned to indicate a duplicate entry
        when(db_manager_mock.createNewAccount(login, pin, holder, starting_balance, "Disabled")).thenReturn(-2);

        CreationResult result = admin_service.createAccount(login, pin, holder, starting_balance, is_active);

        //When createAccount() is called and an account already exists, a CreationResult object should be returned with
        //a DUPLICATE_ACCOUNT status; account numbers of -1 should also match, and createNewAccount() should have been called once
        assertEquals(CreationResult.Status.DUPLICATE_ACCOUNT, result.getStatus());
        assertEquals(-1, result.getAccountNumber());
        verify(db_manager_mock, times(1)).createNewAccount(login, pin, holder, starting_balance, "Disabled");
    }

    @Test
    void test_createAccount_when_account_creation_fails_should_return_error_CreationResult() throws DatabaseException {

        String login = "JD5600";
        String pin = "13579";
        String holder = "John Doe";
        int starting_balance = 0;
        boolean is_active = false;

        //When createNewAccount() called, -2 is to be returned to indicate an error
        when(db_manager_mock.createNewAccount(login, pin, holder, starting_balance, "Disabled")).thenReturn(-1);

        CreationResult result = admin_service.createAccount(login, pin, holder, starting_balance, is_active);

        //When createAccount() is called and account creation fails, a CreationResult object should be returned with
        //an ERROR status; account numbers of -1 should also match, and createNewAccount() should have been called once
        assertEquals(CreationResult.Status.ERROR, result.getStatus());
        assertEquals(-1, result.getAccountNumber());
        verify(db_manager_mock, times(1)).createNewAccount(login, pin, holder, starting_balance, "Disabled");
    }


    @Test
    void test_deleteAccount_when_confirmation_matches_should_return_success_DeletionResult() throws DatabaseException {

        int account_num = 17;
        int confirmation = 17;

        //To prevent getting stuck, db_manager_mock is to do nothing when deleteAccount() is called
        doNothing().when(db_manager_mock).deleteAccount(account_num);

        DeletionResult result = admin_service.deleteAccount(account_num, confirmation);

        //When deleteAccount() is called and runs successfully, a DeletionResult object should be returned with
        //a SUCCESS status; account numbers should also match and deleteAccount() should have been called once
        assertEquals(DeletionResult.Status.SUCCESS, result.getStatus());
        assertEquals(account_num, result.getAccountNumber());
        verify(db_manager_mock, times(1)).deleteAccount(account_num);
    }

    @Test
    void test_deleteAccount_when_confirmation_does_not_match_should_return_confirmation_failure_DeletionResult() throws DatabaseException {

        int account_num = 17;
        int confirmation = 18;

        DeletionResult result = admin_service.deleteAccount(account_num, confirmation);

        //When deleteAccount() is called but the account numbers provided don't match, a DeletionResult object should be returned with
        //a CONFIRMATION_FAILURE status; account numbers should also match and deleteAccount() should have been called zero times
        assertEquals(DeletionResult.Status.CONFIRMATION_FAILURE, result.getStatus());
        assertEquals(account_num, result.getAccountNumber());
        verify(db_manager_mock, times(0)).deleteAccount(account_num);
    }


    @Test
    void test_updateAccount_when_update_is_successful_should_return_true() throws DatabaseException {

        int account_num = 3;
        String new_holder = "John Doe";
        String new_status = "Active";
        String new_login = "JD5500";
        String new_pin = "23456";

        //When updateAccountInfo() is called by db_manager_mock, true is to be returned
        when(db_manager_mock.updateAccountInfo(account_num, new_holder, new_status, new_login, new_pin)).thenReturn(true);

        boolean result = admin_service.updateAccount(account_num, new_holder, new_status, new_login, new_pin);

        //When updateAccount() is called and account successfully updates, true should be returned. updateAccountInfo()
        //should also have been called once
        assertTrue(result);
        verify(db_manager_mock, times(1)).updateAccountInfo(account_num, new_holder, new_status, new_login, new_pin);
    }


    @Test
    void test_searchAccount_when_account_exists_should_return_AccountInfo() throws DatabaseException {

        int account_num = 4;

        //db_manager is to be mocked to return both account_mock and user_mock
        Account account_mock = mock(Account.class);
        when(db_manager_mock.getAccount(account_num)).thenReturn(account_mock);
        User user_mock = mock(User.class);
        when(db_manager_mock.getUser(account_num)).thenReturn(user_mock);

        AccountInfo result = admin_service.searchAccount(account_num);

        //When searchAccount() is called and account exists, the information returned should match with account_mock
        //and user_mock. getAccount() and getUser() should have also been called once each
        assertEquals(account_mock, result.getAccount());
        assertEquals(user_mock, result.getUser());
        verify(db_manager_mock, times(1)).getAccount(account_num);
        verify(db_manager_mock, times(1)).getUser(account_num);
    }

    @Test
    void test_searchAccount_when_account_does_not_exist_should_return_null() throws DatabaseException {

        int account_num = 5;

        //db_manager is mocked to return null when getAccount() is called, to indicate account does not exist
        when(db_manager_mock.getAccount(account_num)).thenReturn(null);

        AccountInfo result = admin_service.searchAccount(account_num);

        //When searchAccount() is called and account does not exist, result should be equal to null.
        //getAccount() should have also been called once
        assertNull(result);
        verify(db_manager_mock, times(1)).getAccount(account_num);
    }

}
