import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class main {
    public static void main(String[] args) {
        // Customer customer = new Customer("C001", "Alice Smith", "alice@example.com");
        // Product product1 = new Product("Lipstick", "Matte lipstick", 19.99, 4.5, "P001");
        // Product product2 = new Product("Foundation", "Liquid foundation", 25.99, 4.8, "P002");
        // Inventory inventory = new Inventory(100);

        // System.out.println("Customer ID: " + customer.getCustomerID());
        // System.out.println("Product Name: " + product1.getName());
        // System.out.println("Stock Level: " + inventory.getStockLevel());

        TerminalInterface terminalInterface = new TerminalInterface();
        terminalInterface.start();
    }
}
