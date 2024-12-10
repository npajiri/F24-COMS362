import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {
    private final ProductManagement productManagement;
    private final ReviewSystem reviewSystem;

    public AdminMenu() {
        this.productManagement = new ProductManagement();
        this.reviewSystem = new ReviewSystem();
    }

    public void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View All Products");
            System.out.println("2. View All Reviews");
            System.out.println("3. Delete a Product");
            System.out.println("4. Delete a Review");
            System.out.println("5. View System Summary");
            System.out.println("6. Manage Inventory");
            System.out.println("7. Review Customer Orders");
            System.out.println("8. View Company Finances");
            System.out.println("9. View Reports"); 
            System.out.println("10. Manage Worker Contracts");
            System.out.println("11. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleViewAllProducts();
                    break;

                case 2:
                    handleViewAllReviews();
                    break;

                case 3:
                    handleDeleteProduct(scanner);
                    break;

                case 4:
                    handleDeleteReview(scanner);
                    break;

                case 5:
                    handleViewSystemSummary();
                    break;

                case 6:
                    handleInventoryManagement(scanner);
                    break;

                case 7:
                    handleReviewCustomerOrders(scanner);
                    break;

                case 8:
                    handleViewFinances();
                    break;

                case 9:
                    handleReports(scanner); 
                    break;

                case 10:
                    manageWorkerContracts(scanner);
                    break;

                case 11:
                    System.out.println("Exiting to main menu...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleViewAllProducts() {
        System.out.println("\n=== View All Products ===");
        productManagement.viewAllProducts(reviewSystem);
    }

    private void handleViewAllReviews() {
        System.out.println("\n=== View All Reviews ===");
        reviewSystem.displayProductReviews();
    }

    private void handleDeleteProduct(Scanner scanner) {
        System.out.println("\n=== Delete a Product ===");
        System.out.print("Enter Product ID to delete: ");
        String productId = scanner.nextLine();

        boolean deleted = productManagement.deleteProduct(productId);
        if (deleted) {
            System.out.println("Product with ID " + productId + " has been deleted successfully.");
        } else {
            System.out.println("Product with ID " + productId + " does not exist.");
        }
    }

    private void handleDeleteReview(Scanner scanner) {
        System.out.println("\n=== Delete a Review ===");
        System.out.print("Enter Product ID to delete reviews for: ");
        String productId = scanner.nextLine();

        boolean deleted = reviewSystem.deleteReviewsForProduct(productId);
        if (deleted) {
            System.out.println("All reviews for Product ID " + productId + " have been deleted.");
        } else {
            System.out.println("No reviews found for Product ID " + productId + ".");
        }
    }

    private void handleViewSystemSummary() {
        System.out.println("\n=== System Summary ===");
        System.out.println("Total Products: " + productManagement.getTotalProducts());
        System.out.println("Total Reviews: " + reviewSystem.getTotalReviews());
    }

    private void handleInventoryManagement(Scanner scanner) {
    boolean managing = true;

    while (managing) {
        System.out.println("\n=== Inventory Management ===");
        System.out.println("1. View Low Stock Products");
        System.out.println("2. Create Supplier Order");
        System.out.println("3. Update Delivery Status");
        System.out.println("4. Exit to Admin Menu");
        System.out.print("Enter your choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Clear invalid input
            continue;
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleViewLowStockProducts();
                break;

            case 2:
                handleCreateSupplierOrder(scanner);
                break;

            case 3:
                handleUpdateDeliveryStatus(scanner);
                break;

            case 4:
                System.out.println("Returning to Admin Menu...");
                managing = false;
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

private void handleViewLowStockProducts() {
    System.out.println("\n=== Low Stock Products ===");
    Map<String, Product> lowStockProducts = productManagement.getLowStockProducts(5); // Threshold of 5
    if (lowStockProducts.isEmpty()) {
        System.out.println("No products are below the stock threshold.");
    } else {
        for (Product product : lowStockProducts.values()) {
            System.out.println(product);
        }
    }
}

private void handleCreateSupplierOrder(Scanner scanner) {
    System.out.println("\n=== Create Supplier Order ===");
    handleViewLowStockProducts();
    System.out.println("\n========================");
    System.out.print("Enter Product ID to order: ");
    String productId = scanner.nextLine();

    Product product = productManagement.getProductById(productId);
    if (product == null) {
        System.out.println("Error: Product ID not found.");
        return;
    }

    System.out.print("Enter quantity to order: ");
    int quantity = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    // Calculate the total cost based on the product price and quantity
    double totalCost = product.getPrice() * quantity;

    // Generate a unique order ID using the current timestamp
    String orderID = "ORD" + System.currentTimeMillis();

    // Create a new SupplierOrder instance
    SupplierOrder order = new SupplierOrder(new Date(), "Pending", totalCost, orderID);

    // Save the order using ProductManagement
    productManagement.createSupplierOrder(order, productId, quantity);
    System.out.println("Order created successfully. Order ID: " + order.getOrderID());
}


private void handleUpdateDeliveryStatus(Scanner scanner) {
    System.out.println("\n=== Update Delivery Status ===");
    System.out.print("Enter Order ID: ");
    String orderId = scanner.nextLine();

    System.out.print("Enter new delivery ETA (yyyy-MM-dd): ");
    String eta = scanner.nextLine();

    boolean updated = productManagement.updateOrderStatus(orderId, "Confirmed", eta);
    if (updated) {
        System.out.println("Order status updated successfully.");
    } else {
        System.out.println("Order ID not found.");
    }
}

private void handleReviewCustomerOrders(Scanner scanner) {
    boolean reviewing = true;

    while (reviewing) {
        System.out.println("\n=== Review Orders ===");
        System.out.println("1. View Pending Orders");
        System.out.println("2. View Shipped Orders");
        System.out.println("3. Ship an Order");
        System.out.println("4. Exit to Admin Menu");
        System.out.print("Enter your choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Clear invalid input
            continue;
        }

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleViewOrdersByStatus("Pending");
                break;

            case 2:
                handleViewOrdersByStatus("Shipped");
                break;

            case 3:
                handleShipOrder(scanner);
                break;

            case 4:
                reviewing = false;
                System.out.println("Returning to Admin Menu...");
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

private void handleViewOrdersByStatus(String status) {
    System.out.println("\n=== " + status + " Orders ===");
    try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
        String line;
        boolean hasOrders = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[3].equalsIgnoreCase(status)) {
                hasOrders = true;
                String orderID = parts[0];
                String orderDate = parts[1];
                double totalCost = Double.parseDouble(parts[2]);

                System.out.printf("Order ID: %s | Date: %s | Total: $%.2f%n", orderID, orderDate, totalCost);
            }
        }

        if (!hasOrders) {
            System.out.println("No " + status.toLowerCase() + " orders found.");
        }
    } catch (IOException e) {
        System.err.println("Error reading orders file: " + e.getMessage());
    }
}

private void handleShipOrder(Scanner scanner) {
    handleViewOrdersByStatus("Pending");

    System.out.println("\n=== Ship an Order ===");
    System.out.print("Enter Order ID to ship: ");
    String orderID = scanner.nextLine();

    boolean updated = updateOrderStatus(orderID, "Shipped");
    if (updated) {
        System.out.println("Order " + orderID + " has been marked as shipped.");
    } else {
        System.out.println("Order ID not found or already shipped.");
    }
}

private boolean updateOrderStatus(String orderID, String newStatus) {
    List<String> updatedLines = new ArrayList<>();
    boolean found = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(orderID) && parts[3].equalsIgnoreCase("Pending")) {
                // Update the order status but preserve product IDs
                String updatedLine = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + newStatus + "|" + parts[4];
                updatedLines.add(updatedLine);
                found = true;
            } else {
                updatedLines.add(line); // Keep the line unchanged if it's not the target order
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading orders file: " + e.getMessage());
        return false;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt"))) {
        for (String updatedLine : updatedLines) {
            writer.write(updatedLine);
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error updating orders file: " + e.getMessage());
        return false;
    }

    return found;
}


private void handleViewFinances() {
    Financials financials = new Financials();
    double earnings = financials.calculateCustomerEarnings();
    double spending = financials.calculateSupplierSpending();

    // Save the current financial state to the file
    financials.saveFinancials(earnings, spending);

    // Display the summary
    System.out.println("\n=== Financial Summary ===");
    System.out.printf("Total Earnings: $%.2f%n", earnings);
    System.out.printf("Total Spending: $%.2f%n", spending);
    System.out.printf("Net Profit: $%.2f%n", earnings - spending);

    // Optionally display the full financial history
    financials.viewFinancialHistory();
    }

    private void handleReports(Scanner scanner) {
    System.out.println("\n=== Reports ===");

    System.out.println("Select Report Type:");
    System.out.println("1. Sales Report");
    System.out.println("2. Inventory Report");
    System.out.print("Enter your choice: ");
    int reportTypeChoice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    String reportType;
    if (reportTypeChoice == 1) {
        reportType = "sales";
    } else if (reportTypeChoice == 2) {
        reportType = "inventory";
    } else {
        System.out.println("Invalid report type. Returning to menu.");
        return;
    }

    System.out.println("Set Parameters:");

    // Date Range
    System.out.print("Enter start date (yyyy-MM-dd): ");
    String startDate = scanner.nextLine();
    System.out.print("Enter end date (yyyy-MM-dd): ");
    String endDate = scanner.nextLine();

    // Specific Category (Optional for Sales)
    String category = "";
    if (reportType.equalsIgnoreCase("sales")) {
        System.out.print("Enter category (or press Enter to skip): ");
        category = scanner.nextLine();
    }

    // Prepare parameters for filtering
    Map<String, String> parameters = new HashMap<>();
    parameters.put("startDate", startDate);
    parameters.put("endDate", endDate);
    if (!category.isEmpty()) {
        parameters.put("category", category);
    }

    Database database = new Database();
    try {
        // Fetch data based on the report type
        List<Map<String, Object>> data = database.retrieveData(reportType, parameters);

        if (data.isEmpty()) {
            System.out.println("No data available for the selected parameters. Please try again.");
            return;
        }

        // Generate and display the report
        System.out.println("\n=== Generated Report ===");
        if (reportType.equalsIgnoreCase("sales")) {
            displaySalesReport(data);
        } else if (reportType.equalsIgnoreCase("inventory")) {
            displayInventoryReport(data);
        }

        // Options to export, print, or download
        System.out.println("\nOptions:");
        System.out.println("1. Export Report");
        System.out.println("2. Print Report");
        System.out.println("3. Download Report");
        System.out.print("Enter your choice: ");
        int action = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (action) {
            case 1:
                System.out.println("Exporting report...");
                // Placeholder for export logic
                break;
            case 2:
                System.out.println("Printing report...");
                break;
            case 3:
                System.out.println("Downloading report...");
                break;
            default:
                System.out.println("Invalid option. Returning to menu.");
        }
    } catch (DatabaseException e) {
        System.out.println("Error retrieving data: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }
}

private void displaySalesReport(List<Map<String, Object>> data) {
    double totalRevenue = 0;
    int totalUnitsSold = 0;
    Map<String, Integer> productSales = new HashMap<>();

    for (Map<String, Object> record : data) {
        // Retrieve and validate fields
        String productName = (String) record.get("productName");
        Double price = record.get("price") != null ? (Double) record.get("price") : 0.0;
        Integer unitsSold = record.get("unitsSold") != null ? (Integer) record.get("unitsSold") : 0;

        if (productName == null) {
            productName = "Unknown Product";
        }

        // Aggregate revenue and sales
        totalRevenue += price * unitsSold;
        totalUnitsSold += unitsSold;

        // Track product sales for identifying the top-selling product
        productSales.put(productName, productSales.getOrDefault(productName, 0) + unitsSold);
    }

    // Identify the top-selling product
    String topSellingProduct = productSales.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");

    // Display report details
    System.out.printf("Total Revenue: $%.2f%n", totalRevenue);
    System.out.printf("Total Units Sold: %d%n", totalUnitsSold);
    System.out.printf("Top-Selling Product: %s%n", topSellingProduct);
}


private void displayInventoryReport(List<Map<String, Object>> data) {
    System.out.println("Inventory Details:");
    for (Map<String, Object> record : data) {
        String productName = (String) record.get("productName");
        int currentStock = (int) record.get("currentStock");
        double turnoverRate = (double) record.get("turnoverRate");

        System.out.printf("Product: %s | Current Stock: %d | Turnover Rate: %.2f%%%n",
                productName, currentStock, turnoverRate);
    }
}


private void manageWorkerContracts(Scanner scanner) {
    WorkerManagementSystem workerSystem = new WorkerManagementSystem();

    System.out.println("\n=== Manage Worker Contracts ===");
    System.out.print("Enter Worker ID: ");
    String workerId = scanner.nextLine();

    WorkerManagementSystem.Worker worker = workerSystem.getWorkerById(workerId);
    if (worker != null) {
        System.out.println("Worker Found: " + worker.toString());
    } else {
        System.out.println("Worker not found. Adding new worker.");
    }

    System.out.print("Enter Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Contract Type (Full-time, Part-time, Contract): ");
    String contractType = scanner.nextLine();
    System.out.print("Enter Salary: ");
    double salary = scanner.nextDouble();
    System.out.print("Enter Hours Per Week: ");
    int hoursPerWeek = scanner.nextInt();
    scanner.nextLine(); // Consume newline
    System.out.print("Enter Start Date (YYYY-MM-DD): ");
    String startDate = scanner.nextLine();
    System.out.print("Enter End Date (YYYY-MM-DD): ");
    String endDate = scanner.nextLine();

    worker = new WorkerManagementSystem.Worker(workerId, name, contractType, salary, hoursPerWeek, startDate, endDate);
    workerSystem.addOrUpdateWorker(worker);

    System.out.println("Worker contract updated successfully.");
}



    
}
