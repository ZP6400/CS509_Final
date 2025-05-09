import controller.ATMController;
import repository.DatabaseConnection;
import repository.DatabaseManager;
import repository.exception.DatabaseException;
import service.AdminService;
import service.CustomerService;
import ui.ATMView;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ATMApplication {

    public static void main(String[] args) throws DatabaseException {

        //Necessary objects are initialized, including database manager and services.
        //StandardCharsets.UTF_8 guarantees that the program will always interpret input using UTF_8
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        DatabaseConnection db_connection = new DatabaseConnection(
                "jdbc:mysql://localhost:3307/atm_db", "root", "Joyful#83900");
        DatabaseManager db_manager = new DatabaseManager(db_connection);
        CustomerService customer_service = new CustomerService(db_manager);
        AdminService admin_service = new AdminService(db_manager);

        //ATM interface and controller are initialized, and the controller starts the program
        ATMView view = new ATMView(scanner);
        ATMController controller = new ATMController(db_manager, customer_service, admin_service, view);
        controller.start();
    }
}
