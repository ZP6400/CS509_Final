package service;

import model.account.Account;
import model.transaction.DepositResult;
import model.transaction.WithdrawalResult;
import model.user.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.DatabaseManager;
import repository.exception.DatabaseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private DatabaseManager db_manager_mock;
    private CustomerService customer_service;
    private Customer customer_mock;
    private Account account_mock;

    @BeforeEach
    public void setUp() {

        db_manager_mock = mock(DatabaseManager.class);
        customer_mock = mock(Customer.class);
        account_mock = mock(Account.class);

        customer_service = new CustomerService(db_manager_mock);
    }


    @Test
    void test_withdrawCash_when_successful_should_return_success_WithdrawalResult() throws DatabaseException {

        //When the following mocks call the following methods, the following should be returned
        when(customer_mock.getAccount()).thenReturn(account_mock);
        when(account_mock.withdraw(100)).thenReturn(true);
        when(account_mock.getaccount_number()).thenReturn(1);
        when(account_mock.getBalance()).thenReturn(900);

        WithdrawalResult result = customer_service.withdrawCash(customer_mock, 100);

        //When withdrawCash() is called and runs successfully, a WithdrawalResult object should be returned with
        //a SUCCESS status; account numbers should also match, and updateAccountBalance() should have been called once
        assertEquals(WithdrawalResult.Status.SUCCESS, result.getStatus());
        assertEquals(1, result.getAccount().getaccount_number());
        verify(db_manager_mock, times(1)).updateAccountBalance(1, 900);
    }

    @Test
    void test_withdrawCash_when_not_enough_cash_in_balance_should_return_insufficient_funds_WithdrawalResult() throws DatabaseException {

        //When the following mocks call the following methods, the following should be returned
        when(customer_mock.getAccount()).thenReturn(account_mock);
        when(account_mock.withdraw(1000)).thenReturn(false);
        when(account_mock.getaccount_number()).thenReturn(2);
        when(account_mock.getBalance()).thenReturn(900);

        WithdrawalResult result = customer_service.withdrawCash(customer_mock, 1000);

        //When withdrawCash() is called and is unable to make the withdrawal, a WithdrawalResult object should be returned with
        //an INSUFFICIENT_FUNDS status; account numbers should also match
        assertEquals(WithdrawalResult.Status.INSUFFICIENT_FUNDS, result.getStatus());
        assertEquals(2, result.getAccount().getaccount_number());
    }

    @Test
    void test_withdrawCash_when_account_does_not_exist_should_return_account_not_found_WithdrawalResult() throws DatabaseException {

        //When customer_mock calls getAccount, null should be returned; the account does not exist
        when(customer_mock.getAccount()).thenReturn(null);

        WithdrawalResult result = customer_service.withdrawCash(customer_mock, 100);

        //When withdrawCash() is called and is unable to make the withdrawal, a WithdrawalResult object should be returned with
        //an ACCOUNT_NOT_FOUND status; account should also be asserted to be null
        assertEquals(WithdrawalResult.Status.ACCOUNT_NOT_FOUND, result.getStatus());
        assertNull(result.getAccount());
    }


    @Test
    void test_depositCash_when_successful_should_return_success_DepositResult() throws DatabaseException {

        //When the following mocks call the following methods, the following should be returned
        when(customer_mock.getAccount()).thenReturn(account_mock);
        when(account_mock.getaccount_number()).thenReturn(17);
        when(account_mock.getBalance()).thenReturn(1100);

        DepositResult result = customer_service.depositCash(customer_mock, 100);

        //When depositCash() is called and runs successfully, a DepositResult object should be returned with
        //a SUCCESS status; account numbers should also match, and updateAccountBalance() should have been called once
        assertEquals(DepositResult.Status.SUCCESS, result.getStatus());
        assertEquals(17, result.getAccount().getaccount_number());
        verify(db_manager_mock, times(1)).updateAccountBalance(17, 1100);
    }

    @Test
    void test_depositCash_when_account_does_not_exist_should_return_account_not_found_DepositResult() throws DatabaseException {

        //When customer_mock calls getAccount, null should be returned; the account does not exist
        when(customer_mock.getAccount()).thenReturn(null);

        DepositResult result = customer_service.depositCash(customer_mock, 100);

        //When depositCash() is called and is unable to make the deposit, a DepositResult object should be returned with
        //an ACCOUNT_NOT_FOUND status; account should also be asserted to be null
        assertEquals(DepositResult.Status.ACCOUNT_NOT_FOUND, result.getStatus());
        assertNull(result.getAccount());
    }
}
