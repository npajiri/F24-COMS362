import java.io.*;
import java.util.Date;

public class Financials {
    private static final String FINANCES_FILE = "finances.txt";

    // Calculate total customer earnings from orders
    public double calculateCustomerEarnings() {
        double earnings = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                earnings += Double.parseDouble(parts[2]); // Total cost
            }
        } catch (IOException e) {
            System.err.println("Error calculating earnings: " + e.getMessage());
        }
        return earnings;
    }

    // Calculate total supplier spending from supplier orders
    public double calculateSupplierSpending() {
        double spending = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader("supplier_orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                spending += Double.parseDouble(parts[3]); // Total cost
            }
        } catch (IOException e) {
            System.err.println("Error calculating spending: " + e.getMessage());
        }
        return spending;
    }

    // Save financial data to a file
    public void saveFinancials(double earnings, double spending) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FINANCES_FILE, true))) {
            String date = new Date().toString();
            writer.write(String.format("Date: %s | Earnings(Customer Orders): $%.2f | Spending(Supplier Orders): $%.2f%n", date, earnings, spending));
        } catch (IOException e) {
            System.err.println("Error saving financials: " + e.getMessage());
        }
    }

    // View financial history from the file
    public void viewFinancialHistory() {
        System.out.println("\n=== Financial History ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(FINANCES_FILE))) {
            String line;
            boolean hasEntries = false;
            while ((line = reader.readLine()) != null) {
                hasEntries = true;
                System.out.println(line);
            }
            if (!hasEntries) {
                System.out.println("No financial records found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading financial history: " + e.getMessage());
        }
    }
}
