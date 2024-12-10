import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerManagement {
    private static final String CUSTOMERS_FILE = "customers.txt";
    private final Map<String, String> customers; // Map of Customer ID to Name

    public CustomerManagement() {
        customers = new HashMap<>();
        loadCustomersFromFile();
    }

    // Load customers from file
    private void loadCustomersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String id = parts[0];
                    String name = parts[1];
                    customers.put(id, name);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Customer file not found. A new file will be created.");
        } catch (IOException e) {
            System.err.println("Error reading customers file: " + e.getMessage());
        }
    }

    // Save customers to file
    private void saveCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Map.Entry<String, String> entry : customers.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving customers file: " + e.getMessage());
        }
    }

    // Add a new customer
    public boolean addCustomer(String id, String name) {
        if (customers.containsKey(id)) {
            System.out.println("Customer ID already exists. Please use a unique ID.");
            return false;
        }
        customers.put(id, name);
        saveCustomersToFile();
        return true;
    }

    // Check if a customer exists
    public boolean customerExists(String id) {
        return customers.containsKey(id);
    }

    // Get customer name by ID
    public String getCustomerName(String id) {
        return customers.get(id);
    }

    // Display all customers
    public void displayAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("Customer ID | Customer Name");
            for (Map.Entry<String, String> entry : customers.entrySet()) {
                System.out.printf("%-12s | %s%n", entry.getKey(), entry.getValue());
            }
        }
    }
}
