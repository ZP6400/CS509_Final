package repository;

import model.account.Account;
import model.user.Administrator;
import model.user.Customer;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.exception.DatabaseException;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DatabaseManagerTest {

    private DatabaseConnection db_connection_mock;
    private DatabaseManager db_manager;

    private Connection connection_mock;
    private PreparedStatement statement_mock;
    private ResultSet result_set_mock;

    @BeforeEach
    public void setUp() {

        db_connection_mock = mock(DatabaseConnection.class);
        connection_mock = mock(Connection.class);
        statement_mock = mock(PreparedStatement.class);
        result_set_mock = mock(ResultSet.class);

        when(db_connection_mock.url()).thenReturn("jdbc:mysql://localhost/test");
        when(db_connection_mock.username()).thenReturn("test_name");
        when(db_connection_mock.password()).thenReturn("test_pass");

        db_manager = new DatabaseManager(db_connection_mock) {

            @Override
            public Connection getConnection() {

                return connection_mock;
            }
        };
    }


    @Test
    public void test_getUser_when_customer_provided_should_correctly_assign_info() throws DatabaseException, SQLException {

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, mocked return values are to be provided
        when(result_set_mock.next()).thenReturn(true);
        when(result_set_mock.getString("role")).thenReturn("Customer");
        when(result_set_mock.getInt("account_num")).thenReturn(1);
        when(result_set_mock.getString("holder")).thenReturn("John Doe");
        when(result_set_mock.getInt("balance")).thenReturn(3000);
        when(result_set_mock.getString("status")).thenReturn("Active");

        User user = db_manager.getUser("JD6100", "12345");

        //When getUser() is run, it should determine that the user is a Customer and correctly assign the proper
        //information to both the customer and account objects
        assertInstanceOf(Customer.class, user);
        Customer customer = (Customer) user;
        assertEquals("JD6100", customer.getLogin());
        assertEquals("12345", customer.getPin());
        Account account = customer.getAccount();
        assertEquals(1, account.getaccount_number());
        assertEquals("John Doe", account.getHolderName());
        assertEquals(3000, account.getBalance());
        assertEquals("Active", account.getStatus());
    }

    @Test
    public void test_getUser_when_administrator_provided_should_correctly_assign_info() throws DatabaseException, SQLException {

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, mocked return values are to be provided
        when(result_set_mock.next()).thenReturn(true);
        when(result_set_mock.getString("role")).thenReturn("Admin");

        User user = db_manager.getUser("admin_user", "admin_pin");

        //When getUser() is run, it should determine that the user is an Administrator and correctly assign the proper
        //information to the user object
        assertInstanceOf(Administrator.class, user);
        assertEquals("admin_user", user.getLogin());
        assertEquals("admin_pin", user.getPin());
    }

    @Test
    public void test_getUser_when_no_user_found_should_return_null() throws DatabaseException, SQLException {

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, false is to be returned; no user is found
        when(result_set_mock.next()).thenReturn(false);

        User user = db_manager.getUser("nonexistent_user", "57473");

        //When getUser() is run, it should determine that the user is null/doesn't exist
        assertNull(user);
    }

    @Test
    public void test_getUser_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        //When prepareStatement() is run, an SQLException is to occur
        when(connection_mock.prepareStatement(any())).thenThrow(new SQLException("Database error"));


        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.getUser("user", "pin");
        });

        //When getUser() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error retrieving user with login: user"));
        assertInstanceOf(SQLException.class, exception.getCause());
    }


    @Test
    public void test_getUser_by_account_num_when_customer_provided_should_correctly_assign_info() throws DatabaseException, SQLException {

        int account_num = 17;

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, mocked return values are to be provided
        when(result_set_mock.next()).thenReturn(true);
        when(result_set_mock.getString("login")).thenReturn("JD6000");
        when(result_set_mock.getString("pin")).thenReturn("12345");
        when(result_set_mock.getString("role")).thenReturn("Customer");
        when(result_set_mock.getInt("account_num")).thenReturn(account_num);
        when(result_set_mock.getString("holder")).thenReturn("John Doe");
        when(result_set_mock.getInt("balance")).thenReturn(4000);
        when(result_set_mock.getString("status")).thenReturn("Active");

        User user = db_manager.getUser(account_num);

        //When getUser() is run, it should determine that the user is a Customer and correctly assign the proper
        //information to both the customer and account objects
        assertInstanceOf(Customer.class, user);
        Customer customer = (Customer) user;
        assertEquals("JD6000", customer.getLogin());
        assertEquals("12345", customer.getPin());
        Account account = customer.getAccount();
        assertEquals(account_num, account.getaccount_number());
        assertEquals("John Doe", account.getHolderName());
        assertEquals(4000, account.getBalance());
        assertEquals("Active", account.getStatus());
    }

    @Test
    public void test_getUser_by_account_num_when_administrator_provided_should_correctly_assign_info() throws DatabaseException, SQLException {

        int account_num = 18;

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, mocked return values are to be provided
        when(result_set_mock.next()).thenReturn(true);
        when(result_set_mock.getString("login")).thenReturn("admin_user");
        when(result_set_mock.getString("pin")).thenReturn("98765");
        when(result_set_mock.getString("role")).thenReturn("Admin");

        User user = db_manager.getUser(account_num);

        //When getUser() is run, it should determine that the user is an Administrator and correctly assign the proper
        //information to the user object
        assertInstanceOf(Administrator.class, user);
        Administrator admin = (Administrator) user;
        assertEquals("admin_user", admin.getLogin());
        assertEquals("98765", admin.getPin());
    }

    @Test
    public void test_getUser_by_account_num_when_no_user_found_should_return_null() throws DatabaseException, SQLException {

        int account_num = 19;

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, false is to be returned; no user is found
        when(result_set_mock.next()).thenReturn(false);

        User user = db_manager.getUser(account_num);

        //When getUser() is run, it should determine that the user is null/doesn't exist
        assertNull(user);
    }

    @Test
    public void test_getUser_by_account_number_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        int account_num = 20;

        //When prepareStatement() is run, an SQLException is to occur
        when(connection_mock.prepareStatement(any())).thenThrow(new SQLException("Database failure"));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.getUser(account_num);
        });

        //When getUser() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error retrieving user with account number: " + account_num));
        assertInstanceOf(SQLException.class, exception.getCause());
    }


    @Test
    public void test_getAccount_when_successful_should_return_account() throws DatabaseException, SQLException {

        int account_num = 21;

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, mocked return values are to be provided
        when(result_set_mock.next()).thenReturn(true);
        when(result_set_mock.getInt("account_num")).thenReturn(account_num);
        when(result_set_mock.getString("holder")).thenReturn("Jane Doe");
        when(result_set_mock.getInt("balance")).thenReturn(9000);
        when(result_set_mock.getString("status")).thenReturn("Disabled");

        Account account = db_manager.getAccount(account_num);

        //When getAccount() is run, it should return the account with the same information that matches
        //the account number that was provided
        assertEquals(account_num, account.getaccount_number());
        assertEquals("Jane Doe", account.getHolderName());
        assertEquals(9000, account.getBalance());
        assertEquals("Disabled", account.getStatus());
    }

    @Test
    public void test_getAccount_when_account_not_found_should_return_null() throws DatabaseException, SQLException {

        int account_num = 22;

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, false is to be returned; no account is found
        when(result_set_mock.next()).thenReturn(false);

        Account account = db_manager.getAccount(account_num);

        //When getAccount() is run, it should determine that the account is null/doesn't exist
        assertNull(account);
    }

    @Test
    public void test_getAccount_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        int account_num = 23;

        //When prepareStatement() is run, an SQLException is to occur
        when(connection_mock.prepareStatement(any())).thenThrow(new SQLException("Database error"));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.getAccount(account_num);
        });

        //When getAccount() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error retrieving account with account number: " + account_num));
        assertInstanceOf(SQLException.class, exception.getCause());
    }


    @Test
    public void test_updateAccountBalance_when_successful_should_execute_update() throws DatabaseException, SQLException {

        int account_num = 1;
        int new_balance = 5000;

        when(connection_mock.prepareStatement(any())).thenReturn(statement_mock);
        db_manager.updateAccountBalance(account_num, new_balance);

        //When updateAccountBalance() is called, the statement_mock should set the following fields and should
        //execute the update below
        verify(statement_mock).setInt(1, new_balance);
        verify(statement_mock).setInt(2, account_num);
        verify(statement_mock).executeUpdate();
    }

    @Test
    public void test_updateAccountBalance_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        int account_num = 2;
        int new_balance = 5000;

        //When prepareStatement() is run, an SQLException is to occur
        when(connection_mock.prepareStatement(any())).thenThrow(new SQLException("Database error"));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.updateAccountBalance(account_num, new_balance);
        });

        //When updateAccountBalance() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error updating account balance"));
        assertInstanceOf(SQLException.class, exception.getCause());
    }


    @Test
    public void test_createNewAccount_when_successful_should_return_account_number() throws DatabaseException, SQLException {

        //When prepareStatement() and getGeneratedKeys() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement_mock);
        when(statement_mock.getGeneratedKeys()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, mocked return values are to be provided
        when(result_set_mock.next()).thenReturn(true);
        when(result_set_mock.getInt(1)).thenReturn(17);

        int result = db_manager.createNewAccount("john_doe", "17", "John Doe", 100000, "Active");

        //When createNewAccount() is called and is successful, it should return the account number of the new user.
        //It should also be verified that statement_mock called executeUpdate() and getGeneratedKeys()
        assertEquals(17, result);
        verify(statement_mock).executeUpdate();
        verify(statement_mock).getGeneratedKeys();
    }

    @Test
    public void test_createNewAccount_when_duplicate_entry_should_return_negative_2() throws DatabaseException, SQLException {

        //When prepareStatement() is run, an SQLException is to occur, specifically the duplicate entry error
        SQLException duplicate_exception = new SQLException("Duplicate entry", "23000", 1062);
        when(connection_mock.prepareStatement(any(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(duplicate_exception);

        int result = db_manager.createNewAccount("jane_doe", "12345", "Jane Doe", 1000, "Active");

        //When createNewAccount() is called, the result should be -2, indicating a duplicate entry
        assertEquals(-2, result);
    }

    @Test
    public void test_createNewAccount_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        //When prepareStatement() is run, an SQLException is to occur
        SQLException general_exception = new SQLException("Database error", "HY000", 999);
        when(connection_mock.prepareStatement(any(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(general_exception);

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.createNewAccount("john_doe", "12345", "John Doe", 850, "Disabled");
        });

        //When updateAccountBalance() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error creating account"));
        assertInstanceOf(SQLException.class, exception.getCause());
    }

    @Test
    public void test_createNewAccount_when_no_generated_key_should_return_negative_1() throws DatabaseException, SQLException {

        //When prepareStatement() and getGeneratedKeys() are run, mocks are to be returned
        when(connection_mock.prepareStatement(any(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement_mock);
        when(statement_mock.getGeneratedKeys()).thenReturn(result_set_mock);

        //When result_set_mock performs the following, false is returned; no generated key is provided
        when(result_set_mock.next()).thenReturn(false);

        int result = db_manager.createNewAccount("jane_doe", "54321", "Jane Doe", 950, "Active");

        //When createNewAccount() is called, the result should be -1, indicating that a new account was not properly made
        assertEquals(-1, result);
    }


    @Test
    public void test_deleteAccount_when_successful_should_call_execute_update() throws DatabaseException, SQLException {

        //When prepareStatement() and executeUpdate() are run, statement_mock and 1 should be returned respectively
        when(connection_mock.prepareStatement(anyString())).thenReturn(statement_mock);
        when(statement_mock.executeUpdate()).thenReturn(1);

        db_manager.deleteAccount(17);

        //When deleteAccount() is called and executed without problem, it should be verifiable that executeUpdate()
        //was called by statement_mock
        verify(statement_mock).executeUpdate();
    }

    @Test
    public void test_deleteAccount_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        //When prepareStatement() is run, an SQLException is to occur
        when(connection_mock.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.deleteAccount(17);
        });

        //When updateAccountBalance() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error deleting account"));
        assertInstanceOf(SQLException.class, exception.getCause());
    }


    @Test
    public void test_updateAccountInfo_when_successful_should_return_true() throws DatabaseException, SQLException {

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(anyString())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //The following mocks the account existing and rows being affected being greater than 0
        when(result_set_mock.next()).thenReturn(true);
        when(statement_mock.executeUpdate()).thenReturn(1);

        boolean result = db_manager.updateAccountInfo(
                1, "John Doe", "Disabled", "JD5900", "12345");

        //When updateAccountInfo() is called and executed without problem, true should be returned
        assertTrue(result);
    }

    @Test
    public void test_updateAccountInfo_when_no_fields_are_updated_should_return_false() throws DatabaseException, SQLException {

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(anyString())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //The following mocks the account existing and rows being affected being equal to 0
        when(result_set_mock.next()).thenReturn(true);
        when(statement_mock.executeUpdate()).thenReturn(0);

        boolean result = db_manager.updateAccountInfo(2, "", "", "", "");

        //When updateAccountInfo() is called but no fields are updated, false should be returned
        assertFalse(result);
    }

    @Test
    public void test_updateAccountInfo_when_account_does_not_exist_should_return_false() throws DatabaseException, SQLException {

        //When prepareStatement() and executeQuery() are run, mocks are to be returned
        when(connection_mock.prepareStatement(anyString())).thenReturn(statement_mock);
        when(statement_mock.executeQuery()).thenReturn(result_set_mock);

        //The following mocks the account being nonexistent
        when(result_set_mock.next()).thenReturn(false);

        boolean result = db_manager.updateAccountInfo(
                3, "Jane Doe", "Disabled", "JD5800", "67890");

        //When updateAccountInfo() is called but the account doesn't exist, false should be returned
        assertFalse(result);
    }

    @Test
    public void test_updateAccountInfo_when_SQLException_occurs_should_throw_DatabaseException() throws SQLException {

        //When prepareStatement() is run, an SQLException is to occur
        when(connection_mock.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {

            db_manager.updateAccountInfo(4, "John Doe", "Active", "JD5700", "57294");
        });

        //When updateAccountInfo() is run, the thrown error should contain the message below, and should also be an
        //instance of SQLException
        assertTrue(exception.getMessage().contains("Error updating account info"));
        assertInstanceOf(SQLException.class, exception.getCause());
    }
}
