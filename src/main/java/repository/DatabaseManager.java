package repository;

import model.account.Account;
import model.user.Administrator;
import model.user.Customer;
import model.user.User;
import repository.exception.DatabaseException;

import java.sql.*;

public class DatabaseManager {

    //The Connection object is initialized as an attribute of this class. It will be used to
    //run MySQL queries
    private final DatabaseConnection db_connection;


    public DatabaseManager(DatabaseConnection db_connection) {

        this.db_connection = db_connection;
    }


    public Connection getConnection() throws SQLException {

        try {

            //Loads the MySQL JDBC driver class to ensure the driver is registered with DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException error) {

            //In the event of an error, the JDBC driver class can't be found
            throw new RuntimeException(error);
        }

        //Uses stored info in db_connection to request a new connection
        return DriverManager.getConnection(db_connection.url(), db_connection.username(), db_connection.password()
        );
    }

    public User getUser(String login, String pin) throws DatabaseException {

        //PreparedStatements exist to execute queries with parameters that can be set dynamically
        try (Connection connection = getConnection();
             PreparedStatement query = connection.prepareStatement(
                     "SELECT * FROM accounts WHERE login = ? AND pin = ?")) {

            //The first placeholder is replaced by the username, and the second placeholder is
            //replaced by the pin code.
            query.setString(1, login);
            query.setString(2, pin);

            //The result is stored in another java.sql object called the ResultSet
            ResultSet result = query.executeQuery();

            //If there is at least one column in the result that is provided:
            if (result.next()) {

                //The value under the "role" column is retrieved, as well the account number
                String role = result.getString("role");

                //If the role of the account matches that of "Customer":
                if ("Customer".equals(role)) {

                    Account customer_account = new Account(
                            result.getInt("account_num"),
                            result.getString("holder"),
                            result.getInt("balance"),
                            result.getString("status"));

                    //A new instance of a customer object is returned with the same information as
                    //before provided, only now with the addition of the user's account number
                    return new Customer(login, pin, customer_account);
                }
                //If the role of the account matches that of "Admin":
                else if ("Admin".equals(role)) {

                    //A new instance of an administrator object is returned with the same information
                    //as before provided
                    return new Administrator(login, pin);
                }
            }
        }
        //In the event of an SQLException, the error is caught:
        catch (SQLException error) {

            throw new DatabaseException("Error retrieving user with login: " + login, error);
        }

        //In the event that there is no user that matches the credentials provided, null is returned
        return null;
    }

    public User getUser(int account_num) throws DatabaseException {

        //A prepared statement is made to find the user with the specific account_num provided
        try (Connection connection = getConnection();
             PreparedStatement query = connection.prepareStatement(
                     "SELECT * FROM accounts WHERE account_num = ?")) {

            //The placeholder for account_num is filled in with the value provided in the parameter
            query.setInt(1, account_num);

            //The query gets executed, and the result is stored in the ResultSet object
            ResultSet result = query.executeQuery();

            //If the result is not null:
            if (result.next()) {

                //The value under the "login" column is retrieved, as well the pin and role
                String login = result.getString("login");
                String pin = result.getString("pin");
                String role = result.getString("role");

                //If the role of the account matches that of "Customer":
                if ("Customer".equals(role)) {
                    Account customer_account = new Account(
                            result.getInt("account_num"),
                            result.getString("holder"),
                            result.getInt("balance"),
                            result.getString("status")
                    );

                    return new Customer(login, pin, customer_account);
                }
                //If the role of the account matches that of "Admin":
                else if ("Admin".equals(role)) {

                    return new Administrator(login, pin);
                }
            }
        }
        //In the event of an SQLException, the error is caught:
        catch (SQLException error) {

            throw new DatabaseException("Error retrieving user with account number: " + account_num, error);
        }

        //In the event that there is no user that matches the credentials provided, null is returned
        return null;
    }


        public Account getAccount(int account_num) throws DatabaseException {

        //A prepared statement is made to find the account with the specific account_num provided
        try (Connection connection = getConnection();
             PreparedStatement query = connection.prepareStatement(
                     "SELECT * FROM accounts WHERE account_num = ?")) {

            //The placeholder for account_num is filled in with the value provided in the parameter
            query.setInt(1, account_num);

            //The query gets executed, and the result is stored in the ResultSet object
            ResultSet result = query.executeQuery();

            //If the result is not null:
            if (result.next()) {

                //A new Account object is returned with the same information as the account
                //that was returned in the result
                return new Account(result.getInt("account_num"), result.getString("holder"),
                        result.getInt("balance"), result.getString("status"));
            }
        }
        catch (SQLException error) {

            throw new DatabaseException("Error retrieving account with account number: " + account_num, error);
        }

        return null;
    }


    public void updateAccountBalance(int account_num, int new_balance) throws DatabaseException {

        //A prepared statement is made to update an existing account in the database
        try (Connection connection = getConnection();
             PreparedStatement update = connection.prepareStatement(
                     "UPDATE accounts SET balance = ? WHERE account_num = ?")) {

            //The placeholders are now filled with the provided new_balance and account_num
            update.setInt(1, new_balance);
            update.setInt(2, account_num);

            update.executeUpdate();
        }
        catch (SQLException error) {

            throw new DatabaseException("Error updating account balance", error);
        }
    }


    public int createNewAccount(String login, String pin, String holder, int balance, String status)
            throws DatabaseException {

        //A prepared statement is made to insert a new account into the database
        try (Connection connection = getConnection();
             PreparedStatement update = connection.prepareStatement(
                "INSERT INTO accounts (" +
                        "holder, balance, status, login, pin, role) VALUES (?, ?, ?, ?, ?, 'Customer')",
                     Statement.RETURN_GENERATED_KEYS)) {

            //The placeholders are now filled with the provided username, pin code, holder name, starting
            //balance, and status of the account respectively
            update.setString(1, holder);
            update.setInt(2, balance);
            update.setString(3, status);
            update.setString(4, login);
            update.setString(5, pin);

            update.executeUpdate();

            //The getGeneratedKeys() function is then called, which returns the auto-incremented primary
            //key of the table; in this case, it is the account_number
            ResultSet result = update.getGeneratedKeys();

            //If an updated account_number gets returned, which in this case, next() returns true:
            if (result.next()) {

                //The value from the first column of the first row is retrieved and returned; in this case,
                //it is the account number of the newly created account
                return result.getInt(1);
            }
        }
        catch (SQLException error) {

            if (error.getErrorCode() == 1062) {

                //If the error code is that of 1062, mySQL detects a duplicate entry; -2 is to be returned
                return -2;
            }
            else {

                //Otherwise, a different DatabaseException error occurred
                throw new DatabaseException("Error creating account", error);
            }
        }

        //In the event that a new account was not properly made, -1 is returned
        return -1;
    }

    public void deleteAccount(int account_num) throws DatabaseException {

        //A prepared statement is made where the account with the account number provided is to be deleted
        try (Connection connection = getConnection();
             PreparedStatement update = connection.prepareStatement(
                     "DELETE FROM accounts WHERE account_num = ?")) {

            //The placeholder is filled in with said account number, and the deletion is then performed
            update.setInt(1, account_num);
            update.executeUpdate();
        }
        catch (SQLException error) {

            throw new DatabaseException("Error deleting account", error);
        }
    }

    public boolean updateAccountInfo(int account_num, String new_holder,
                                     String new_status, String new_login, String new_pin) throws DatabaseException {

        //These will be filled in with the data of the account with the account number provided momentarily
        String current_holder = "";
        String current_status = "";
        String current_login = "";
        String current_pin = "";

        //A prepared statement is made to obtain the holder name, current status, login, and pin code of the account
        //with the specific number provided
        try (Connection connection = getConnection();
             PreparedStatement query = connection.prepareStatement(
                     "SELECT holder, status, login, pin FROM accounts WHERE account_num = ?")) {

            //The placeholder is filled with said account number and then the query is executed, the
            //result being stored in a ResultSet object
            query.setInt(1, account_num);
            ResultSet result = query.executeQuery();

            if (result.next()) {

                //Store strings found in 'result' in their respective variables
                current_holder = result.getString("holder");
                current_status = result.getString("status");
                current_login = result.getString("login");
                current_pin = result.getString("pin");
            }
        }
        catch (SQLException error) {

            throw new DatabaseException("Error updating account info", error);
        }

        //For each of the data that can be updated. If the string passed in for the field is empty:
        if (new_holder.isEmpty()) {

            //No update was made, so the data for that field remains the same
            new_holder = current_holder;
        }

        //This goes for the rest of the account info fields as well
        if (new_status.isEmpty()) {

            new_status = current_status;
        }
        if (new_login.isEmpty()) {

            new_login = current_login;
        }
        if (new_pin.isEmpty()) {

            new_pin = current_pin;
        }

        //Another prepared statement is made to update the account with the account number provided, specifically
        //its holder, status, login, and pin code fields, whether they remain unchanged or not
        try (Connection connection = getConnection();
             PreparedStatement update = connection.prepareStatement(
                     "UPDATE accounts SET holder = ?, status = ?, login = ?, pin = ? WHERE account_num = ?")) {

            //The placeholders are filled in with their respective strings
            update.setString(1, new_holder);
            update.setString(2, new_status);
            update.setString(3, new_login);
            update.setString(4, new_pin);
            update.setInt(5, account_num);

            //The update is then executed; what is returned is the number of rows within the table
            //of the database that were impacted
            int rows_affected = update.executeUpdate();

            //If the number of rows is greater than 0, a change occurred, meaning the table was
            //updated successfully and that thus the account was updated successfully. Otherwise,
            //no change occurred, meaning that the account number provided by the user did not belong to any account
            return rows_affected > 0;
        }
        catch (SQLException error) {

            throw new DatabaseException("Error updating account info", error);
        }
    }
}