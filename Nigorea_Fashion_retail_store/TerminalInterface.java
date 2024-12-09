import java.util.Scanner;

public class TerminalInterface {
    public void start() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("=== Welcome to the Nigorea Fashion Store ===");
            System.out.println("Select your role:");
            System.out.println("1. Customer");
            System.out.println("2. Supplier");
            System.out.println("3. Admin");
            System.out.println("4. Employee");
            System.out.println("5. Customer Service Agent");
            System.out.println("6. Exit Store");
            System.out.print("Enter your choice: ");

            int roleChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (roleChoice) {
                case 1:
                    CustomerMenu customerMenu = new CustomerMenu();
                    customerMenu.showMenu(scanner);
                    break;

                case 2:
                    SupplierMenu supplierMenu = new SupplierMenu();
                    supplierMenu.showMenu(scanner);
                    break;

                case 3:
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.showMenu(scanner);
                    break;

                case 4:
                    EmployeeMenu employeeMenu = new EmployeeMenu();
                    employeeMenu.showMenu(scanner);
                    break;

                case 5:
                    System.out.println("Customer Service Agent options are not yet implemented.");
                    break;

                case 6:
                    System.out.println("Thanks for stopping by! We hope to see you again.....");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Exiting.");
            }
        }
        scanner.close();
    
    }
}
