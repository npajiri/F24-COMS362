import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private final ReviewSystem reviewSystem;
    private final ProductManagement productManagement;
    private final Cart cart;

    public CustomerMenu() {
        this.reviewSystem = new ReviewSystem();
        this.productManagement = new ProductManagement();
        this.cart = new Cart();
    }

    public void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Write a Review");
            System.out.println("2. View Reviews for a Product");
            System.out.println("3. List All Reviewed Products");
            System.out.println("4. Display All Products and Reviews");
            System.out.println("5. Add Product to Cart");
            System.out.println("6. View Cart");
            System.out.println("7. Checkout");
            System.out.println("8. View Order History");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleWriteReview(scanner);
                    break;

                case 2:
                    handleViewReviews(scanner);
                    break;

                case 3:
                    handleListReviewedProducts();
                    break;

                case 4:
                    handleDisplayAllReviews();
                    break;

                case 5:
                    handleAddToCart(scanner);
                    break;

                case 6:
                    handleViewCart();
                    break;

                case 7:
                    handleCheckout(scanner);
                    break;

                case 8:
                    handleViewOrderHistory();
                    break;
                
                case 9:
                    running = false;
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleWriteReview(Scanner scanner) {
        System.out.println("\n=== Products Available for Review ===");
        displayOrderedAndShippedProducts(); // Display eligible products

        System.out.print("Enter Product ID (e.g P001, P002, etc): ");
        String productId = scanner.nextLine();


        // Verify if the product was ordered and shipped
        if (!verifyProductOrderedAndShipped(productId)) {
            System.out.println("Error: You can only review products that you have ordered and have been shipped.");
            return;
        }


        System.out.print("Enter Review Text: ");
        String reviewText = scanner.nextLine();

        System.out.print("Enter Rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating. Please enter a value between 1 and 5.");
        } else {
            reviewSystem.writeReview(productId, reviewText, rating);
        }
    }

    private void displayOrderedAndShippedProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            boolean hasProducts = false;
    
            System.out.printf("%-10s %-20s%n", "Product ID", "Product Name");
            System.out.println("---------------------------------");
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String shippingStatus = parts[3];
    
                // Only consider shipped orders
                if (shippingStatus.equalsIgnoreCase("Shipped")) {
                    String[] productIds = parts.length > 4 ? parts[4].split(",") : new String[0];
                    for (String productId : productIds) {
                        Product product = productManagement.getProductById(productId);
                        if (product != null) {
                            hasProducts = true;
                            System.out.printf("%-10s %-20s%n", productId, product.getName());
                        }
                    }
                }
            }
    
            if (!hasProducts) {
                System.out.println("No products available for review.");
            }
        } catch (IOException e) {
            System.err.println("Error reading orders file: " + e.getMessage());
        }
    }
    

    private boolean verifyProductOrderedAndShipped(String productId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String orderID = parts[0];
                String shippingStatus = parts[3];
    
                // If the order is shipped, check if the product is part of it
                if (shippingStatus.equalsIgnoreCase("Shipped")) {
                    String[] orderedProducts = parts.length > 4 ? parts[4].split(",") : new String[0];
                    for (String orderedProduct : orderedProducts) {
                        if (orderedProduct.equals(productId)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error verifying product order: " + e.getMessage());
        }
        return false;
    }
    

    private void handleViewReviews(Scanner scanner) {
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();

        System.out.println("\nReviews for Product ID: " + productId);
        reviewSystem.getProductReviews(productId).forEach(System.out::println);
    }

    private void handleListReviewedProducts() {
        System.out.println("\nReviewed Products:");
        reviewSystem.listReviewedProducts().forEach(System.out::println);
    }

    private void handleDisplayAllReviews() {
        System.out.println("\nProduct reviews:");
        reviewSystem.displayProductReviews();
    }

    private void handleAddToCart(Scanner scanner) {
        // Display all available products
        System.out.println("\n=== Available Products ===");
        productManagement.viewAllProducts(reviewSystem);

        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();

        Product product = productManagement.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if quantity is within available stock
        if (quantity > product.getStock()) {
            System.out.println("Error: Requested quantity exceeds available stock. Available stock: " + product.getStock());
            return;
        }

        cart.addItem(productId, quantity, product.getPrice());
        System.out.println("Added " + quantity + " of " + product.getName() + " to cart.");
    }

    private void handleViewCart() {
        System.out.println("\n=== Cart ===");
        cart.getItems().forEach((productId, quantity) -> {
            Product product = productManagement.getProductById(productId);
            if (product != null) {
                System.out.println(product.getName() + " (" + productId + "): " + quantity);
            }
        });
        System.out.println("Total Cost: $" + cart.getTotalCost());
    }

    private void handleCheckout(Scanner scanner) {
        System.out.println("\n=== Checkout ===");
    
        // Print receipt header
        System.out.println("\n=== Receipt ===");
        System.out.printf("%-10s %-20s %-10s %-10s%n", "Product ID", "Product Name", "Quantity", "Price");
    
        // Process cart items
        cart.getItems().forEach((productId, quantity) -> {
            Product product = productManagement.getProductById(productId);
            if (product != null) {
                // Update stock levels
                productManagement.updateStock(productId, -quantity);
    
                // Print product details for receipt
                System.out.printf("%-10s %-20s %-10d $%-10.2f%n", productId, product.getName(), quantity, product.getPrice() * quantity);
            }
        });

        StringBuilder productIdsBuilder = new StringBuilder(); // To store product IDs in the order
        cart.getItems().forEach((productId, quantity) -> {
            Product product = productManagement.getProductById(productId);
            if (product != null) {
                //productManagement.updateStock(productId, -quantity); // Update stock levels
    
                // Append product ID to the order's product list
                if (productIdsBuilder.length() > 0) {
                    productIdsBuilder.append(",");
                }
                productIdsBuilder.append(productId);
            }
        });
    
        // Calculate total cost and generate order ID
        double totalCost = cart.getTotalCost();
        String orderID = "ORD" + System.currentTimeMillis();
        String productIds = productIdsBuilder.toString(); // Get the product IDs as a comma-separated string
    
        // Save the order
        Order order = new Order(new Date(), totalCost, "Pending", orderID);
        saveOrder(order, productIds);
    
        // Generate a report
        String reportID = "RPT" + System.currentTimeMillis();
        Report report = new Report(reportID, "Customer Order", new Date());
        saveReport(report, totalCost);
    
        // Print total cost and order ID
        System.out.printf("%n%-10s %-20s $%-10.2f%n", "", "Total:", totalCost);
        System.out.println("Order ID: " + orderID);
    
        // Clear the cart and finalize checkout
        cart.clearCart();
        System.out.println("Checkout complete. Thank you for your purchase!");
    }
    

    private void saveOrder(Order order, String productIds) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(order.getOrderID() + "|" + order.getOrderDate() + "|" +
                         order.getTotalCost() + "|" + order.getShippingStatus() + "|" + productIds);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving order: " + e.getMessage());
        }
    }

    private void saveReport(Report report, double totalCost) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reports.txt", true))) {
            writer.write(report.getReportID() + "|" + report.getReportType() + "|" +
                         report.getDateGenerated() + "|Total Cost: $" + totalCost);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving report: " + e.getMessage());
        }
    }
    
    private void handleViewOrderHistory() {
        System.out.println("\n=== Order History ===");
        Scanner scanner = new Scanner(System.in);
    
        // Create an instance of OrderHistoryManager
        OrderHistoryManager orderHistoryManager = new OrderHistoryManager();
    
        // Prompt customer for their ID
        System.out.print("Enter your Customer ID (1, 2, or 3): ");
        String customerId = scanner.nextLine();
    
        // Determine which file to use based on customerId
        String ordersFile;
        switch (customerId) {
            case "1":
                ordersFile = "orders.txt";
                break;
            case "2":
                ordersFile = "orders1.txt";
                break;
            case "3":
                ordersFile = "orders2.txt";
                break;
            default:
                System.out.println("Invalid Customer ID. Please try again.");
                return;
        }
    
        // Variables to store filter and sort preferences
        int savedFilterChoice = 5; // Default: Skip Filters
        int savedSortChoice = 0;   // Default: No Sorting
        List<Order> filteredOrders = null;
    
        while (true) {
            // Retrieve full order history for the customer
            List<Order> orders = orderHistoryManager.getOrderHistory(customerId, ordersFile);
            if (orders.isEmpty()) {
                System.out.println("No past orders found. Start shopping now!");
                return;
            }
    
            // Apply saved filters and sorting
            if (savedFilterChoice != 5) {
                filteredOrders = orderHistoryManager.applyFilters(orders, savedFilterChoice, scanner);
            } else {
                filteredOrders = orders;
            }
    
            if (savedSortChoice > 0) {
                filteredOrders = orderHistoryManager.sortOrders(filteredOrders, savedSortChoice);
            }
    
            // Display the filtered and sorted order history
            System.out.println("\n=== Order History ===");
            displayOrders(filteredOrders);
    
            // Prompt user to view order details or modify filters
            System.out.println("\nOptions:");
            System.out.println("1. Apply Filters");
            System.out.println("2. Search by Order ID");
            System.out.println("3. Select an Order to View Details");
            System.out.println("4. Reset Filters");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int action = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (action) {
                case 1:
                    // Apply Filters
                    System.out.println("Apply Filters:");
                    System.out.println("1. Date Range");
                    System.out.println("2. Order Status");
                    System.out.println("3. Price Range");
                    System.out.println("4. Skip Filters");
                    System.out.print("Enter your choice: ");
                    savedFilterChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    break;
    
                case 2:
                    // Search by Order ID
                    System.out.print("Enter an Order ID to search: ");
                    String keyword = scanner.nextLine();
                    filteredOrders = orderHistoryManager.searchOrders(orders, keyword);
                    savedFilterChoice = 5; // Ignore other filters when searching by Order ID
                    break;
    
                case 3:
                    // Select an Order to View Details
                    System.out.print("Enter the number of the order to view details (or 0 to go back): ");
                    int orderChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
    
                    if (orderChoice > 0 && orderChoice <= filteredOrders.size()) {
                        Order selectedOrder = filteredOrders.get(orderChoice - 1);
                        while (true) {
                            // Display order details
                            orderHistoryManager.displayOrderDetails(selectedOrder);
    
                            // Prompt user for next action
                            System.out.println("Press 'P' to print, 'D' to download, or 'B' to go back:");
                            String postAction = scanner.nextLine().toUpperCase();
    
                            if ("P".equals(postAction)) {
                                System.out.println("Printing order details...");
                                continue; // Redisplay order details
                            } else if ("D".equals(postAction)) {
                                System.out.println("Downloading order details...");
                                continue; // Redisplay order details
                            } else if ("B".equals(postAction)) {
                                break; // Return to order history
                            } else {
                                System.out.println("Invalid option. Returning to order details.");
                            }
                        }
                    } else {
                        System.out.println("Invalid choice. Returning to order history.");
                    }
                    break;
    
                case 4:
                    // Reset Filters
                    savedFilterChoice = 5; // No filters
                    savedSortChoice = 0;   // No sorting
                    System.out.println("Filters have been reset.");
                    break;
    
                case 5:
                    // Exit
                    System.out.println("Exiting order history.");
                    return;
    
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
    
    // Helper method to display orders
    private void displayOrders(List<Order> orders) {
        System.out.printf("%-5s %-15s %-10s %-10s%n", "No.", "Order Date", "Total Cost", "Status");
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            System.out.printf("%-5d %-15s $%-10.2f %-10s%n", i + 1, order.getOrderDate(), order.getTotalCost(), order.getShippingStatus());
        }
    }
    

    
    

    
}
