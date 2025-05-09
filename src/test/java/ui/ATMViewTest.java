package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ATMViewTest {

    private ByteArrayOutputStream output_stream;
    private ATMView atm_view;

    @BeforeEach
    void setUp() {

        output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
    }


    @Test
    void test_displayMessage_when_given_message_should_print_it_to_console() {

        atm_view = new ATMView(new Scanner(System.in));
        atm_view.displayMessage("Welcome to the ATM!");

        //When displayMessage() is run, the output printed to the terminal should
        //be "Welcome to the ATM!"
        String output = output_stream.toString().trim();
        assertTrue(output.contains("Welcome to the ATM!"));
    }

    @Test
    void test_displayError_when_given_message_should_print_it_to_console() {

        atm_view = new ATMView(new Scanner(System.in));
        atm_view.displayError("Database error");

        //When displayError() is run, the output printed to the terminal should
        //be "The following error occurred: Database error"
        String output = output_stream.toString().trim();
        assertTrue(output.contains("The following error occurred: Database error"));
    }


    @Test
    void test_promptLogin_when_given_username_should_be_accurate_to_what_was_inputted() {

        //ByteArrayInputStream is used to simulate a user inputting a string into the console
        ByteArrayInputStream input = new ByteArrayInputStream("john_doe\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        String result = atm_view.promptLogin();

        //When promptLogin() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString().trim();
        assertTrue(output.contains("Enter Login:"));
        assertEquals("john_doe", result);
    }

    @Test
    void test_promptNewLogin_when_given_username_should_be_accurate_to_what_was_inputted() {

        //ByteArrayInputStream is used to simulate a user inputting a string into the console
        ByteArrayInputStream input = new ByteArrayInputStream("john_doe_new\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        String result = atm_view.promptNewLogin();

        //When promptNewLogin() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString().trim();
        assertTrue(output.contains("Enter new Login:"));
        assertEquals("john_doe_new", result);
    }

    @Test
    void test_promptPin_when_given_invalid_pin_and_then_valid_pin_should_notify_user_and_then_accept_second_pin() {

        //Username has to be inputted first, then incorrect pin, then correct pin
        ByteArrayInputStream input = new ByteArrayInputStream("username\n1234\n12345\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        String result = atm_view.promptPin();

        //When promptPin() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Enter Pin:"));
        assertTrue(output.contains("Invalid pin. Please enter exactly 5 digits."));
        assertEquals("12345", result);
    }

    @Test
    void test_promptNewPin_when_given_invalid_pin_and_then_valid_pin_should_notify_user_and_then_accept_second_pin() {

        //Username has to be inputted first, then incorrect pin, then correct pin
        ByteArrayInputStream input = new ByteArrayInputStream("username\n1234\n12345\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        String result = atm_view.promptNewPin();

        //When promptNewPin() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Enter new Pin:"));
        assertTrue(output.contains("Invalid pin. Please enter exactly 5 digits."));
        assertEquals("12345", result);
    }

    @Test
    void test_promptHolderName_when_given_holder_name_should_be_accurate_to_what_was_inputted() {

        //A \n has to be inputted first, then the holder's name
        ByteArrayInputStream input = new ByteArrayInputStream("\nJohn Doe\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        String result = atm_view.promptHolderName();

        //When promptHolderName() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Enter Holder's Name:"));
        assertEquals("John Doe", result);
    }

    @Test
    void test_promptNewHolderName_when_given_holder_name_should_be_accurate_to_what_was_inputted() {

        //A \n has to be inputted first, then the holder's name
        ByteArrayInputStream input = new ByteArrayInputStream("\nJane Doe\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        String result = atm_view.promptNewHolderName();

        //When promptNewHolderName() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Enter new Holder's Name:"));
        assertEquals("Jane Doe", result);
    }

    @Test
    void test_promptStartingBalance_when_given_invalid_value_and_then_valid_value_should_notify_user_and_then_accept_second_input() {

        //Invalid integer is inputted first, then a valid integer
        ByteArrayInputStream input = new ByteArrayInputStream("-5000\n5000\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        int result = atm_view.promptStartingBalance();

        //When promptStartingBalance() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Enter Starting Balance:"));
        assertTrue(output.contains("Starting balance must be a positive number. Please try again."));
        assertEquals(5000, result);
    }

    @Test
    void test_promptAccountStatus_when_given_invalid_string_and_then_Y_string_should_notify_user_and_then_return_true() {

        //Invalid string is inputted first, then a valid string
        ByteArrayInputStream input = new ByteArrayInputStream("maybe\nY\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        boolean result = atm_view.promptAccountStatus();

        //When promptAccountStatus() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Is the Account Active? (Y/N):"));
        assertTrue(output.contains("Invalid input. Please enter 'Y' or 'N'."));
        assertTrue(result);
    }

    @Test
    void test_promptAccountStatus_when_given_invalid_string_and_then_N_string_should_notify_user_and_then_return_false() {

        //Invalid string is inputted first, then a valid string
        ByteArrayInputStream input = new ByteArrayInputStream("maybe\nn\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        boolean result = atm_view.promptAccountStatus();

        //When promptAccountStatus() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Is the Account Active? (Y/N):"));
        assertTrue(output.contains("Invalid input. Please enter 'Y' or 'N'."));
        assertFalse(result);
    }

    @Test
    void test_promptNewStatus_when_given_invalid_string_and_then_Y_string_should_notify_user_and_then_return_Active() {

        //Invalid string is inputted first, then a valid string
        ByteArrayInputStream input = new ByteArrayInputStream("maybe\nY\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        String result = atm_view.promptNewStatus();

        //When promptNewAccountStatus() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Is the Account Active? (Y/N):"));
        assertTrue(output.contains("Invalid input. Please enter 'Y' or 'N'."));
        assertEquals("Active", result);
    }

    @Test
    void test_promptNewStatus_when_given_invalid_string_and_then_N_string_should_notify_user_and_then_return_Disabled() {

        //Invalid string is inputted first, then a valid string
        ByteArrayInputStream input = new ByteArrayInputStream("maybe\nn\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        String result = atm_view.promptNewStatus();

        //When promptNewAccountStatus() is run and the user input is fed, the following assertions should be true
        String output = output_stream.toString();
        assertTrue(output.contains("Is the Account Active? (Y/N):"));
        assertTrue(output.contains("Invalid input. Please enter 'Y' or 'N'."));
        assertEquals("Disabled", result);
    }


    @Test
    void test_displayCustomerMenu_when_run_should_display_menu_text_for_customers() {

        Scanner scanner = new Scanner(System.in);
        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));

        atm_view.displayCustomerMenu();

        //When displayCustomerMenu() is called, the output should contain the following lines of text
        String output = output_stream.toString();
        assertTrue(output.contains("1----Withdraw Cash"));
        assertTrue(output.contains("2----Deposit Cash"));
        assertTrue(output.contains("3----Display Balance"));
        assertTrue(output.contains("4----Exit"));
    }

    @Test
    void test_displayAdminMenu_when_run_should_display_menu_text_for_admins() {

        Scanner scanner = new Scanner(System.in);
        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));

        atm_view.displayAdminMenu();

        //When displayAdminMenu() is called, the output should contain the following lines of text
        String output = output_stream.toString();
        assertTrue(output.contains("1----Create New Account"));
        assertTrue(output.contains("2----Delete Existing Account"));
        assertTrue(output.contains("3----Update Account Information"));
        assertTrue(output.contains("4----Search for Account"));
        assertTrue(output.contains("5----Exit"));
    }

    @Test
    void test_displayUpdateChoice_when_run_should_display_menu_for_options_on_updating_account() {

        Scanner scanner = new Scanner(System.in);
        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));

        atm_view.displayUpdateChoice();

        //When displayAdminMenu() is called, the output should contain the following lines of text
        String output = output_stream.toString();
        assertTrue(output.contains("Select the field to update:"));
        assertTrue(output.contains("1----Update Holder’s Name"));
        assertTrue(output.contains("2----Update Status"));
        assertTrue(output.contains("3----Update Login"));
        assertTrue(output.contains("4----Update Pin Code"));
        assertTrue(output.contains("5----Exit"));
    }

    @Test
    void test_promptMenuChoice_when_given_invalid_choice_and_then_valid_choice_should_notify_user_and_then_accept_second_choice() {

        //Invalid choice is inputted first, then a valid choice
        ByteArrayInputStream input = new ByteArrayInputStream("invalid\n4\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        int choice = atm_view.promptMenuChoice();

        //When promptMenuChoice() is run and the user input is fed, the following assertions should be true
        assertEquals(4, choice);
        assertTrue(output_stream.toString().contains("Invalid input. Please enter a valid integer."));
        assertTrue(output_stream.toString().contains("Enter choice:"));
    }


    @Test
    void test_promptWithdrawal_when_given_invalid_amount_and_then_negative_amount_and_then_valid_amount_should_notify_user_twice_and_then_accept_third_choice() {

        //Invalid choice is inputted first, then a valid choice
        ByteArrayInputStream input = new ByteArrayInputStream("invalid\n-50\n200\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        int withdrawal_amount = atm_view.promptWithdrawal();

        //When promptWithdrawal() is run and the user input is fed, the following assertions should be true
        assertEquals(200, withdrawal_amount);
        assertTrue(output_stream.toString().contains("Invalid input. Please enter a positive integer."));
        assertTrue(output_stream.toString().contains("Enter the withdrawal amount:"));
    }

    @Test
    void test_promptDeposit_when_given_invalid_amount_and_then_negative_amount_and_then_valid_amount_should_notify_user_twice_and_then_accept_third_choice() {

        //Invalid choice is inputted first, then a valid choice
        ByteArrayInputStream input = new ByteArrayInputStream("invalid\n-50\n200\n".getBytes());
        Scanner scanner = new Scanner(input);

        ATMView atm_view = new ATMView(scanner);
        ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output_stream));
        int deposit_amount = atm_view.promptDeposit();

        //When promptMenuChoice() is run and the user input is fed, the following assertions should be true
        assertEquals(200, deposit_amount);
        assertTrue(output_stream.toString().contains("Invalid input. Please enter a positive integer."));
        assertTrue(output_stream.toString().contains("Enter the cash amount to deposit: "));
    }
}
