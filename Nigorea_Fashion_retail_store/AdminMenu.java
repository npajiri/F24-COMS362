import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {
    private final ProductManagement productManagement;
    private final ReviewSystem reviewSystem;
    private final CustomerManagement customerManagement;

    public AdminMenu() {
        this.productManagement = new ProductManagement();
        this.reviewSystem = new ReviewSystem();
        this.customerManagement = new CustomerManagement();
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
            System.out.println("9. View Reports"); // New Option
            System.out.println("10. Manage Worker Contracts");
            System.out.println("11. View Performance Report");
            System.out.println("12. Manage Events");
            System.out.println("0. Exit to Main Menu");
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
                    generateWorkerPerformanceReport(scanner);
                    break;   

                case 12:
                    handleManageEvents(scanner);
                    break;  

                case 0:
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
        System.out.println("4. Manage Inventory Inquiries");
        System.out.println("0. Exit to Admin Menu");
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
                handleManageInventoryInquiries(scanner);
                break;

            case 0:
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

private void handleViewOrdersByStatus(String status, String customerId) {
    System.out.println("\n=== " + status + " Orders ===");
    try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
        String line;
        boolean hasOrders = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[3].equalsIgnoreCase(status) && parts[5].equals(customerId)) {
                hasOrders = true;
                String orderID = parts[0];
                String orderDate = parts[1];
                double totalCost = Double.parseDouble(parts[2]);

                System.out.printf("Order ID: %s | Date: %s | Total: $%.2f%n", orderID, orderDate, totalCost);
            }
        }

        if (!hasOrders) {
            System.out.println("No " + status.toLowerCase() + " orders found for Customer ID: " + customerId);
        }
    } catch (IOException e) {
        System.err.println("Error reading orders file: " + e.getMessage());
    }
}

// private void handleViewOrdersByStatus(String status) {
//     handleViewOrdersByStatus(status, null);
// }

private void handleShipOrder(Scanner scanner) {
    System.out.println("Do you want to view:");
    System.out.println("1. All Pending Orders");
    System.out.println("2. Specific Customer's Pending Orders");
    System.out.print("Enter your choice: ");

    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    String customerId = null;

    if(choice == 1){
        handleViewOrdersByStatus("Pending");
    }else if (choice == 2) {
        System.out.print("Enter Customer ID: ");
        customerId = scanner.nextLine();
        
        // Validate that the Customer ID exists
        if (!customerManagement.customerExists(customerId)) {
            System.out.println("Invalid Customer ID. Returning to menu...");
            return;
        }
        
        handleViewOrdersByStatus("Pending", customerId);
    } else{
        System.out.println("Invalid choice. Returning to menu...");
        return;
    }
    


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


// private void handleShipOrder(Scanner scanner) {
//     handleViewOrdersByStatus("Pending");

//     System.out.println("\n=== Ship an Order ===");
//     System.out.print("Enter Order ID to ship: ");
//     String orderID = scanner.nextLine();

//     boolean updated = updateOrderStatus(orderID, "Shipped");
//     if (updated) {
//         System.out.println("Order " + orderID + " has been marked as shipped.");
//     } else {
//         System.out.println("Order ID not found or already shipped.");
//     }
// }

// private boolean updateOrderStatus(String orderID, String newStatus) {
//     List<String> updatedLines = new ArrayList<>();
//     boolean found = false;

//     try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
//         String line;
//         while ((line = reader.readLine()) != null) {
//             String[] parts = line.split("\\|");
//             if (parts[0].equals(orderID) && parts[3].equalsIgnoreCase("Pending")) {
//                 // Update the order status but preserve product IDs
//                 String updatedLine = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + newStatus + "|" + parts[4];
//                 updatedLines.add(updatedLine);
//                 found = true;
//             } else {
//                 updatedLines.add(line); // Keep the line unchanged if it's not the target order
//             }
//         }
//     } catch (IOException e) {
//         System.err.println("Error reading orders file: " + e.getMessage());
//         return false;
//     }

//     try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt"))) {
//         for (String updatedLine : updatedLines) {
//             writer.write(updatedLine);
//             writer.newLine();
//         }
//     } catch (IOException e) {
//         System.err.println("Error updating orders file: " + e.getMessage());
//         return false;
//     }

//     return found;
// }

private boolean updateOrderStatus(String orderID, String newStatus) {
    List<String> updatedLines = new ArrayList<>();
    boolean found = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(orderID) && parts[3].equalsIgnoreCase("Pending")) {
                // Preserve customer ID and other details
                String updatedLine = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + 
                                     newStatus + "|" + parts[4] + "|" + parts[5];
                updatedLines.add(updatedLine);
                found = true;
            } else {
                updatedLines.add(line);
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

    while (true) {
        System.out.println("\n=== Manage Worker Contracts ===");
        System.out.println("1. Find Worker");
        System.out.println("2. Add New Worker");
        System.out.println("3. Remove Worker");
        System.out.println("0. Exit to Admin Menu");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                findAndUpdateWorker(scanner, workerSystem);
                break;
            case "2":
                addNewWorker(scanner, workerSystem);
                break;
            case "3":
                removeWorker(scanner, workerSystem);
                break;
            case "0":
                return; // Exit back to Admin Menu
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

private void findAndUpdateWorker(Scanner scanner, WorkerManagementSystem workerSystem) {
    System.out.print("\nEnter Worker ID: ");
    String workerId = scanner.nextLine();

    WorkerManagementSystem.Worker worker = workerSystem.getWorkerById(workerId);
    if (worker != null) {
        System.out.println("Worker Found: " + worker.toString());
        System.out.println("Do you want to update this worker's details? (yes/no): ");
        String updateChoice = scanner.nextLine().trim().toLowerCase();

        if (!updateChoice.equals("yes")) {
            System.out.println("No updates made to the worker.");
            return; // Exit the method without updating
        }

        System.out.print("Enter Name (" + worker.getName() + "): ");
        String name = scanner.nextLine();
        System.out.print("Enter Position (" + worker.getPosition() + "): ");
        String position = scanner.nextLine();
        System.out.print("Enter Contract Type (" + worker.getContractType() + "): ");
        String contractType = scanner.nextLine();
        System.out.print("Enter Salary (" + worker.getSalary() + "): ");
        double salary = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Hours Per Week (" + worker.getHoursPerWeek() + "): ");
        int hoursPerWeek = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Start Date (" + worker.getStartDate() + "): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter End Date (" + worker.getEndDate() + "): ");
        String endDate = scanner.nextLine();

        // Update the worker with the new details
        worker = new WorkerManagementSystem.Worker(workerId,
                                                   name.isEmpty() ? worker.getName() : name,
                                                   position.isEmpty() ? worker.getPosition() : position,
                                                   contractType.isEmpty() ? worker.getContractType() : contractType,
                                                   salary,
                                                   hoursPerWeek,
                                                   startDate.isEmpty() ? worker.getStartDate() : startDate,
                                                   endDate.isEmpty() ? worker.getEndDate() : endDate);
        workerSystem.addOrUpdateWorker(worker);

        System.out.println("Worker details updated successfully.");
    } else {
        System.out.println("Worker not found. Please try again.");
    }
}

private void addNewWorker(Scanner scanner, WorkerManagementSystem workerSystem) {
    // Automatically generate the next Worker ID
    String newWorkerId = generateNextWorkerId(workerSystem);

    if (newWorkerId == null) {
        System.out.println("Unable to generate a new Worker ID. Please check the workers file.");
        return;
    }

    System.out.println("\nAssigning Worker ID: " + newWorkerId);
    System.out.print("Enter Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Position: ");
    String position = scanner.nextLine();
    System.out.print("Enter Contract Type: ");
    String contractType = scanner.nextLine();
    System.out.print("Enter Salary: ");
    double salary = Double.parseDouble(scanner.nextLine());
    System.out.print("Enter Hours Per Week: ");
    int hoursPerWeek = Integer.parseInt(scanner.nextLine());
    System.out.print("Enter Start Date (YYYY-MM-DD): ");
    String startDate = scanner.nextLine();
    System.out.print("Enter End Date (YYYY-MM-DD): ");
    String endDate = scanner.nextLine();

    WorkerManagementSystem.Worker newWorker = new WorkerManagementSystem.Worker(newWorkerId, name, position, contractType, salary, hoursPerWeek, startDate, endDate);
    workerSystem.addOrUpdateWorker(newWorker);

    System.out.println("New worker added successfully with ID: " + newWorkerId);
}

private String generateNextWorkerId(WorkerManagementSystem workerSystem) {
    List<WorkerManagementSystem.Worker> workers = workerSystem.getAllWorkers();

    // Find the highest numerical Worker ID
    int maxId = 0;
    for (WorkerManagementSystem.Worker worker : workers) {
        try {
            // Extract the numerical part of the Worker ID (e.g., W001 -> 1)
            String workerId = worker.getWorkerId();
            int numericPart = Integer.parseInt(workerId.substring(1));
            if (numericPart > maxId) {
                maxId = numericPart;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid Worker ID format: " + worker.getWorkerId());
        }
    }

    // Generate the next Worker ID
    return "W" + String.format("%03d", maxId + 1);
}
private void removeWorker(Scanner scanner, WorkerManagementSystem workerSystem) {
    System.out.print("\nEnter Worker ID to Remove: ");
    String workerId = scanner.nextLine();

    boolean removed = workerSystem.removeWorker(workerId);
    if (removed) {
        System.out.println("Worker with ID " + workerId + " has been successfully removed.");
    } else {
        System.out.println("Worker with ID " + workerId + " not found.");
    }
}


private void generateWorkerPerformanceReport(Scanner scanner) {
    ReportManager reportManager = new ReportManager(new Database());

    System.out.println("\n=== Generate Worker Performance Report ===");

    // Validate the start and end dates
    String startDate = validateDateInput(scanner, "Enter Start Date (YYYY-MM-DD): ");
    String endDate = validateDateInput(scanner, "Enter End Date (YYYY-MM-DD): ");

    System.out.println("Do you want to filter by worker role? (yes/no): ");
    String filterChoice = scanner.nextLine().trim().toLowerCase();
    String roleFilter = null;
    if (filterChoice.equals("yes")) {
        System.out.print("Enter Worker Role (e.g., Seller, Manager): ");
        roleFilter = scanner.nextLine();
    }

    System.out.println("Do you want to sort the report? (yes/no): ");
    String sortChoice = scanner.nextLine().trim().toLowerCase();
    String sortBy = null;
    if (sortChoice.equals("yes")) {
        System.out.print("Sort by (hours/sales): ");
        sortBy = scanner.nextLine().trim().toLowerCase();
    }

    DetailedReport report = reportManager.generateWorkerPerformanceReport(startDate, endDate, roleFilter, sortBy);

    if (report != null && !report.getDetails().isEmpty()) {
        System.out.println("Report Generated Successfully:");
        System.out.println(report.toString());
    } else {
        System.out.println("No performance data found for the specified date range or criteria.");
    }
}


private String validateDateInput(Scanner scanner, String prompt) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false); // Strict date parsing

    while (true) {
        System.out.print(prompt);
        String inputDate = scanner.nextLine();

        try {
            dateFormat.parse(inputDate); // Validate the date format
            return inputDate; // Return the valid date
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
}

private void handleManageEvents(Scanner scanner) {
    boolean managing = true;

    while (managing) {
        System.out.println("\n=== Manage Events ===");
        System.out.println("1. Manage Fashion Shows");
        System.out.println("2. Manage Launch Parties");
        System.out.println("3. Manage Sale Previews");
        System.out.println("4. Exit to Admin Menu");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleFashionShows(scanner);
                break;
            case 2:
                System.out.println("Launch Parties management is under development.");
                break;
            case 3:
                System.out.println("Sale Previews management is under development.");
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

private void handleFashionShows(Scanner scanner) {
    boolean managingFashionShows = true;

    while (managingFashionShows) {
        System.out.println("\n=== Manage Fashion Shows ===");
        System.out.println("1. View Fashion Show Orders");
        System.out.println("2. Create Work Orders");
        System.out.println("3. Mark Event Orders as Shipped");
        System.out.println("4. Back to Events Management");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                handleFashionShowOrders(scanner);
                break;
            case 2:
                handleCreateWorkOrders(scanner);
                break;
            case 3:
                handleShipping(scanner);
                break;
            case 4:
                System.out.println("Returning to Events Management...");
                managingFashionShows = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

private void handleFashionShowOrders(Scanner scanner) {
    System.out.println("\n=== Fashion Show Orders ===");
    try (BufferedReader reader = new BufferedReader(new FileReader("event_orders.txt"))) {
        String line;
        boolean hasOrders = false;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[1].equalsIgnoreCase("Fashion Show")) { // Filter by event name
                hasOrders = true;
                System.out.println("Customer ID: " + parts[0]);
                System.out.println("Event Name: " + parts[1]);
                System.out.println("Event Order ID: " + parts[2]);
                System.out.println("Outfit Description: " + parts[3]);
                System.out.println("Shipping Status: " + parts[4]);
                System.out.println("-----------------------------------");
            }
        }
        if (!hasOrders) {
            System.out.println("No fashion show orders found.");
        }
    } catch (IOException e) {
        System.err.println("Error reading event orders: " + e.getMessage());
    }
}


// Create Work Orders
private void handleCreateWorkOrders(Scanner scanner) {
    System.out.println("\n=== Create Work Orders ===");
    System.out.print("Enter Registration ID to create a work order: ");
    String registrationId = scanner.nextLine();

    // System.out.print("Assign this order to Employee ID: ");
    // String employeeId = scanner.nextLine();

    //createWorkOrder(registrationId, employeeId);
    // Retrieve outfit description
    String outfitDescription = getOutfitDescriptionFromEventOrders(registrationId);

    if (outfitDescription == null) {
        System.out.println("Registration ID not found. Unable to create work order.");
        return;
    }

    // Create work order
    createWorkOrder(registrationId, outfitDescription);
}

// Retrieve Outfit Description from Event Orders File
private String getOutfitDescriptionFromEventOrders(String eventOrderId) {
    try (BufferedReader reader = new BufferedReader(new FileReader("event_orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[2].equals(eventOrderId)) { // Match event order ID
                return parts[3]; // Outfit description is stored in the fourth column
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading event orders file: " + e.getMessage());
    }
    return null; // Return null if event order ID not found
}

// Save Work Order to File
private void createWorkOrder(String registrationId, String outfitDescription) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("work_orders.txt", true))) {
        writer.write("Registration ID: " + registrationId + " | Outfit Description: " + outfitDescription + " | Status: Pending");
        writer.newLine();
        System.out.println("Work order created successfully!");
    } catch (IOException e) {
        System.err.println("Error saving work order: " + e.getMessage());
    }
}

// private void createWorkOrder(String registrationId, String employeeId) {
//     try (BufferedWriter writer = new BufferedWriter(new FileWriter("work_orders.txt", true))) {
//         writer.write("Registration ID: " + registrationId + " | Assigned to Employee ID: " + employeeId + " | Status: Pending");
//         writer.newLine();
//         System.out.println("Work order created successfully!");
//     } catch (IOException e) {
//         System.err.println("Error saving work order: " + e.getMessage());
//     }
// }

private void handleShipping(Scanner scanner) {
    System.out.println("\n=== Pending Fashion Show Orders ===");
    List<String> updatedOrders = new ArrayList<>();
    boolean hasPending = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("event_orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[1].equalsIgnoreCase("Fashion Show") && parts[4].equalsIgnoreCase("Pending")) {
                hasPending = true;
                System.out.println("Event Order ID: " + parts[2]);
                System.out.println("Customer ID: " + parts[0]);
                System.out.println("Outfit Description: " + parts[3]);
                System.out.println("-----------------------------------");
            }
            updatedOrders.add(line);
        }
        if (!hasPending) {
            System.out.println("No pending fashion show orders found.");
            return;
        }
    } catch (IOException e) {
        System.err.println("Error reading event orders: " + e.getMessage());
        return;
    }

    System.out.print("Enter Event Order ID to mark as shipped: ");
    String eventOrderId = scanner.nextLine();

    // Mark the event order as shipped and notify the customer
    markEventOrderAsShipped(eventOrderId, updatedOrders);
}

// Mark the event order as shipped and notify the customer
private void markEventOrderAsShipped(String eventOrderId, List<String> updatedOrders) {
    boolean found = false;
    String customerId = null;

    for (int i = 0; i < updatedOrders.size(); i++) {
        String line = updatedOrders.get(i);
        String[] parts = line.split("\\|");
        if (parts[2].equalsIgnoreCase(eventOrderId) && parts[4].equalsIgnoreCase("Pending")) {
            found = true;
            customerId = parts[0];
            updatedOrders.set(i, parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|Shipped");
        }
    }

    if (found) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_orders.txt"))) {
            for (String updatedLine : updatedOrders) {
                writer.write(updatedLine);
                writer.newLine();
            }
            System.out.println("Event order marked as shipped.");
        } catch (IOException e) {
            System.err.println("Error updating event orders: " + e.getMessage());
            return;
        }

        // Notify the customer
        notifyCustomer(customerId, eventOrderId);
    } else {
        System.out.println("Event Order ID not found or already shipped.");
    }
}

// Notify the customer
private void notifyCustomer(String customerId, String eventOrderId) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer_notifications.txt", true))) {
        writer.write("Customer ID: " + customerId + " | Event Order ID: " + eventOrderId + " | Status: Shipped" + " | Your event order has been shipped!");
        writer.newLine();
        System.out.println("Notification sent to customer with ID: " + customerId);
    } catch (IOException e) {
        System.err.println("Error sending customer notification: " + e.getMessage());
    }
}


// private void handleShipping(Scanner scanner) {
//     System.out.println("\n=== Pending Fashion Show Orders ===");
//     try (BufferedReader reader = new BufferedReader(new FileReader("event_orders.txt"))) {
//         String line;
//         boolean hasPending = false;
//         while ((line = reader.readLine()) != null) {
//             String[] parts = line.split("\\|");
//             if (parts[1].equalsIgnoreCase("Fashion Show") && parts[4].equalsIgnoreCase("Pending")) {
//                 hasPending = true;
//                 System.out.println("Event Order ID: " + parts[2]);
//                 System.out.println("Customer ID: " + parts[0]);
//                 System.out.println("Outfit Description: " + parts[3]);
//                 System.out.println("-----------------------------------");
//             }
//         }
//         if (!hasPending) {
//             System.out.println("No pending fashion show orders found.");
//             return;
//         }
//     } catch (IOException e) {
//         System.err.println("Error reading event orders: " + e.getMessage());
//     }

//     System.out.print("Enter Event Order ID to mark as shipped: ");
//     String eventOrderId = scanner.nextLine();

//     markEventOrderAsShipped(eventOrderId);
// }


// private void markEventOrderAsShipped(String eventOrderId) {
//     List<String> updatedOrders = new ArrayList<>();
//     boolean found = false;

//     try (BufferedReader reader = new BufferedReader(new FileReader("event_orders.txt"))) {
//         String line;
//         while ((line = reader.readLine()) != null) {
//             String[] parts = line.split("\\|");
//             if (parts[2].equals(eventOrderId)) { // Match event order ID
//                 found = true;
//                 updatedOrders.add(parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|Shipped");
//             } else {
//                 updatedOrders.add(line);
//             }
//         }
//     } catch (IOException e) {
//         System.err.println("Error reading event orders file: " + e.getMessage());
//     }

//     if (found) {
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_orders.txt"))) {
//             for (String updatedOrder : updatedOrders) {
//                 writer.write(updatedOrder);
//                 writer.newLine();
//             }
//             System.out.println("Event order marked as shipped.");
//         } catch (IOException e) {
//             System.err.println("Error updating event orders file: " + e.getMessage());
//         }
//         notifyCustomer(customerId, eventOrderId);
//     } else {
//         System.out.println("Event Order ID not found or already shipped.");
//     }
//     // else {
//     //     System.out.println("Event order ID not found.");
//     // }

//     // Notify the customer
    
// }


private void handleManageInventoryInquiries(Scanner scanner) {
    System.out.println("\n=== Inventory Inquiries ===");
    try (BufferedReader reader = new BufferedReader(new FileReader("inventory_inquiries.txt"))) {
        String line;
        boolean hasInquiries = false;
        List<String> unresolvedInquiries = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            //hasInquiries = true;
            String[] parts = line.split("\\|");
            String status = parts[5];
            System.out.println("Inquiry ID: " + parts[0]);
            System.out.println("Customer ID: " + parts[1]);
            System.out.println("Product ID: " + parts[2]);
            System.out.println("Product Name: " + parts[3]);
            System.out.println("Quantity: " + parts[4]);
            System.out.println("Status: " + status);
            System.out.println("-----------------------------------");

            // Collect unresolved inquiries for possible resolution
            if (status.equalsIgnoreCase("Unresolved")) {
                unresolvedInquiries.add(line);
                hasInquiries = true;
            }
        }

        if (unresolvedInquiries.isEmpty()) {
            System.out.println("No unresolved inquiries to manage.");
            return;
        }

        System.out.print("Enter Inquiry ID to resolve: ");
        String inquiryId = scanner.nextLine();

        // Resolve inquiry
        // Resolve inquiry if unresolved
        for (String unresolvedInquiry : unresolvedInquiries) {
            String[] parts = unresolvedInquiry.split("\\|");
            if (parts[0].equals(inquiryId)) {
                resolveInquiry(inquiryId, parts[1], parts[2], Integer.parseInt(parts[4]));
                return;
            }
        }

        System.out.println("Inquiry ID not found or already resolved.");
    } catch (IOException e) {
        System.err.println("Error reading inventory inquiries: " + e.getMessage());
    }

    //     for (String inquiry : getAllInquiries()) {
    //         String[] parts = inquiry.split("\\|");
    //         if (parts[0].equals(inquiryId)) {
    //             resolveInquiry(inquiryId, parts[1], parts[2], Integer.parseInt(parts[4]));
    //             return;
    //         }
    //     }
    //     System.out.println("Inquiry ID not found.");
    // } catch (IOException e) {
    //     System.err.println("Error reading inventory inquiries: " + e.getMessage());
    // }
}

// Helper to get all inquiries
private List<String> getAllInquiries() {
    List<String> inquiries = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("inventory_inquiries.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            inquiries.add(line);
        }
    } catch (IOException e) {
        System.err.println("Error reading inventory inquiries: " + e.getMessage());
    }
    return inquiries;
}

// Resolve inquiry by transferring stock from a secondary inventory
private void resolveInquiry(String inquiryId, String customerId, String productId, int quantity) {
    try {
        // Get all inventories
        Map<String, Inventory> inventories = productManagement.getAllInventories();

        // Find inventory with sufficient stock
        String sourceInventory = null;
        for (Map.Entry<String, Inventory> entry : inventories.entrySet()) {
            String inventoryName = entry.getKey();
            Inventory inventory = entry.getValue();
            if (inventory.hasStock(productId, quantity)) {
                sourceInventory = inventoryName;
                break;
            }
        }

        if (sourceInventory != null) {
            // Transfer stock to Main inventory
            if (productManagement.transferStock(sourceInventory, "Main", productId, quantity)) {
                System.out.println("Stock transferred successfully from " + sourceInventory + " to Main inventory.");
                // Notify customer and remove the inquiry
                notifyCustomerOfResolvedInquiry(customerId, productId, quantity);
                updateInquiryStatus(inquiryId, "Resolved");
                //removeInquiry(inquiryId);
            } else {
                System.out.println("Error during stock transfer.");
            }
        } else {
            System.out.println("No inventory has sufficient stock to fulfill this inquiry.");
        }
    } catch (Exception e) {
        System.err.println("Error resolving inquiry: " + e.getMessage());
    }
}

// Notify customer of resolved inquiry
private void notifyCustomerOfResolvedInquiry(String customerId, String productId, int quantity) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("customer_notifications.txt", true))) {
        String message = String.format(
            "has been resolved. You can add it to your cart now.",
            customerId, productId, quantity
        );
        writer.write("Customer ID: " + customerId + " | Product ID: " + productId + 
                     " | Quantity: " + quantity + " | " + message);
        writer.newLine();
        System.out.println("Customer has been notified of the resolved inquiry.");
    } catch (IOException e) {
        System.err.println("Error notifying customer: " + e.getMessage());
    }
}

// Update inquiry status
private void updateInquiryStatus(String inquiryId, String newStatus) {
    List<String> updatedInquiries = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader("inventory_inquiries.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(inquiryId)) {
                // Update the status of the matching inquiry
                parts[5] = newStatus;
                updatedInquiries.add(String.join("|", parts));
            } else {
                updatedInquiries.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading inventory inquiries file: " + e.getMessage());
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_inquiries.txt"))) {
        for (String updatedLine : updatedInquiries) {
            writer.write(updatedLine);
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error updating inventory inquiries file: " + e.getMessage());
    }
}


// Remove resolved inquiry from inquiries file
private void removeInquiry(String inquiryId) {
    List<String> updatedInquiries = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("inventory_inquiries.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith(inquiryId + "|")) {
                updatedInquiries.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading inquiries: " + e.getMessage());
        return;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_inquiries.txt"))) {
        for (String updatedInquiry : updatedInquiries) {
            writer.write(updatedInquiry);
            writer.newLine();
        }
        System.out.println("Inquiry resolved and removed from file.");
    } catch (IOException e) {
        System.err.println("Error updating inquiries file: " + e.getMessage());
    }
}



    
}
