package ui;

import model.account.Account;
import model.user.User;

import java.util.Scanner;

public class ATMView {

    private final Scanner scanner;

    public ATMView(Scanner scanner) {

        this.scanner = scanner;
    }


    public void displayMessage(String message) {

        System.out.println(message);
    }

    public void displayError(String message) {

        System.out.println("The following error occurred: " + message);
    }


    public String promptLogin() {

        System.out.print("Enter Login: ");
        return scanner.next();
    }

    public String promptNewLogin() {

        System.out.print("Enter new Login: ");
        return scanner.next();
    }

    public String promptPin() {

        while (true) {

            System.out.print("Enter Pin: ");
            String pin = scanner.next();
            if (pin.length() == 5 && pin.matches("\\d{5}")) {

                return pin;
            }
            System.out.println("Invalid pin. Please enter exactly 5 digits.");
        }
    }

    public String promptNewPin() {

        while (true) {

            System.out.print("Enter new Pin: ");
            String pin = scanner.next();
            if (pin.length() == 5 && pin.matches("\\d{5}")) {

                return pin;
            }
            System.out.println("Invalid pin. Please enter exactly 5 digits.");
        }
    }

    public String promptHolderName() {

        scanner.nextLine();
        System.out.print("Enter Holder's Name: ");
        return scanner.nextLine();
    }

    public String promptNewHolderName() {

        scanner.nextLine();
        System.out.print("Enter new Holder's Name: ");
        return scanner.nextLine();
    }

    public int promptStartingBalance() {

        int balance = -1;
        while (balance < 0) {

            System.out.print("Enter Starting Balance: ");
            balance = scanner.nextInt();
            if (balance < 0) {

                System.out.println("Starting balance must be a positive number. Please try again.");
            }
        }
        return balance;
    }

    public boolean promptAccountStatus() {

        while (true) {

            System.out.print("Is the Account Active? (Y/N): ");
            String status = scanner.next().toLowerCase();
            if (status.equals("y")) {

                return true;
            }
            if (status.equals("n")) {

                return false;
            }

            System.out.println("Invalid input. Please enter 'Y' or 'N'.");
        }
    }

    public String promptNewStatus() {

        while (true) {

            System.out.print("Is the Account Active? (Y/N): ");
            String status = scanner.next().toLowerCase();
            if (status.equals("y")) {

                return "Active";
            }
            if (status.equals("n")) {

                return "Disabled";
            }
            System.out.println("Invalid input. Please enter 'Y' or 'N'.");
        }
    }


    public void displayCustomerMenu() {

        System.out.println("\n1----Withdraw Cash");
        System.out.println("2----Deposit Cash");
        System.out.println("3----Display Balance");
        System.out.println("4----Exit");
    }

    public void displayAdminMenu() {

        System.out.println("\n1----Create New Account");
        System.out.println("2----Delete Existing Account");
        System.out.println("3----Update Account Information");
        System.out.println("4----Search for Account");
        System.out.println("5----Exit");
    }

    public void displayUpdateChoice() {
        System.out.println("\nSelect the field to update:");
        System.out.println("1----Update Holder’s Name");
        System.out.println("2----Update Status");
        System.out.println("3----Update Login");
        System.out.println("4----Update Pin Code");
        System.out.println("5----Exit");
    }

    public int promptMenuChoice() {

        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {

            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next();

            System.out.print("Enter choice: ");
        }
        return scanner.nextInt();
    }


    public int promptWithdrawal() {

        int withdrawal_amount;

        while (true) {

            System.out.print("Enter the withdrawal amount: ");

            if (scanner.hasNextInt()) {

                withdrawal_amount = scanner.nextInt();

                if (withdrawal_amount <= 0) {

                    System.out.println("Invalid input. Please enter a positive integer.");
                }
                else {

                    return withdrawal_amount;
                }
            }
            else {

                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }

    public int promptDeposit() {

        int deposited_amount;

        while (true) {

            System.out.print("Enter the cash amount to deposit: ");

            if (scanner.hasNextInt()) {

                deposited_amount = scanner.nextInt();

                if (deposited_amount <= 0) {

                    System.out.println("Invalid input. Please enter a positive integer.");
                }
                else {

                    return deposited_amount;
                }
            } else {

                System.out.println("Invalid input. Please enter a positive integer.");
                scanner.next();
            }
        }
    }


    public int promptaccount_numberForDeletion() {

        while (true) {

            System.out.print("Enter the account number to which you want to delete: ");

            if (scanner.hasNextInt()) {

                int account_num = scanner.nextInt();

                if (account_num <= 0) {

                    System.out.println("Account number must be greater than 0. Please try again.");
                }
                else {

                    return account_num;
                }
            }
            else {

                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }

    public int confirmDeletion(String holder) {

        while (true) {

            System.out.print("You wish to delete the account held by " + holder + ". " +
                    "If this is correct, please re-enter the account number: ");

            if (scanner.hasNextInt()) {

                return scanner.nextInt();
            }
            else {

                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
        }
    }

    public int promptaccount_number() {

        while (true) {

            System.out.print("Enter the Account number: ");

            if (scanner.hasNextInt()) {

                int account_num = scanner.nextInt();

                if (account_num <= 0) {

                    System.out.println("Account number must be greater than 0. Please try again.");
                }
                else {

                    return account_num;
                }
            }
            else {

                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }

    public void showAccountInfo(Account account, User user) {

        System.out.println("Account #" + account.getaccount_number());
        System.out.println("Holder: " + account.getHolderName());
        System.out.println("Balance: $" + account.getBalance());
        System.out.println("Status: " + account.getStatus());
        System.out.println("Login: " + user.getLogin());
        System.out.println("Pin Code: " + user.getPin());
    }
}
