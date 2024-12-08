import java.util.Scanner;

public class SupplierMenu {
    private final ProductManagement productManagement;
    private final ReviewSystem reviewSystem;

    public SupplierMenu() {
        this.productManagement = new ProductManagement();
        this.reviewSystem = new ReviewSystem();
    }

    public void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Supplier Menu ===");
            System.out.println("1. View Stock Levels");
            System.out.println("2. Supply New Products");
            System.out.println("3. Update Product Details");
            System.out.println("4. View Pending Orders");
            System.out.println("5. Fulfill an Order");
            System.out.println("6. Decline an Order");
            System.out.println("7. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleViewStockLevels();
                    break;

                case 2:
                    handleSupplyProduct(scanner);
                    break;

                case 3:
                    handleUpdateProduct(scanner);
                    break;

                case 4:
                    handleViewPendingOrders();
                    break;

                case 5:
                    handleFulfillOrder(scanner);
                    break;

                case 6:
                    handleDeclineOrder(scanner);
                    break;

                case 7:
                    System.out.println("Returning to main menu...");
                    running = false; // Exit the loop to return to the main menu
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleViewStockLevels() {
        System.out.println("\n=== View Stock Levels ===");
        productManagement.viewAllProducts(null);
    }

    private void handleSupplyProduct(Scanner scanner) {
        System.out.println("\n=== Supply a Product ===");
        System.out.print("Enter Product ID(e.g P001, P002, etc): ");
        String productId = scanner.nextLine();

        // Check if the product ID already exists
        if (productManagement.getProductById(productId) != null) {
            System.out.println("Error: A product with this ID already exists. Please use a different ID.");
            return;
        }

        System.out.print("Enter Product Name: ");
        String productName = scanner.nextLine();

        // Check if the product name already exists
        if (productManagement.getProductByName(productName) != null) {
            System.out.println("Error: A product with this name already exists. Please use a different name.");
            return;
        }

        System.out.print("Enter Product Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter Initial Stock Level: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        productManagement.addProduct(productId, productName, description, price, stock);
        System.out.println("Product supplied successfully!");
    }

    private void handleUpdateProduct(Scanner scanner) {
        System.out.println("\n=== Update Product Details ===");
        System.out.print("Enter Product ID(e.g P001, P002, etc): ");
        String productId = scanner.nextLine();

        System.out.print("Enter Updated Product Name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter Updated Product Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Updated Product Price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter Updated Stock Level: ");
        int stock = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        boolean updated = productManagement.updateProduct(productId, productName, description, price, stock);
        if (updated) {
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Product not found. Update failed.");
        }
    }

    private void handleViewPendingOrders() {
        System.out.println("\n=== Pending Orders ===");
        productManagement.viewPendingSupplierOrders();
    }

    private void handleFulfillOrder(Scanner scanner) {
        System.out.println("\n=== Fulfill an Order ===");
        handleViewPendingOrders();
        System.out.println("\n=============================");
        System.out.print("Enter Order ID to fulfill: ");
        String orderId = scanner.nextLine();

        boolean fulfilled = productManagement.fulfillSupplierOrder(orderId);
        if (fulfilled) {
            System.out.println("Order " + orderId + " fulfilled successfully. Stock levels updated.");
        } else {
            System.out.println("Order ID not found or already processed.");
        }
    }

    // private void handleDeclineOrder(Scanner scanner) {
    //     System.out.println("\n=== Decline an Order ===");
    //     System.out.print("Enter Order ID to decline: ");
    //     String orderId = scanner.nextLine();

    //     boolean declined = productManagement.declineSupplierOrder(orderId);
    //     if (declined) {
    //         System.out.println("Order " + orderId + " has been declined.");
    //     } else {
    //         System.out.println("Order ID not found or already processed.");
    //     }
    // }

    private void handleDeclineOrder(Scanner scanner) {
        System.out.println("\n=== Decline an Order ===");
        System.out.print("Enter Order ID to decline: ");
        String orderId = scanner.nextLine();
    
        double orderCost = productManagement.declineSupplierOrder(orderId);
        if (orderCost >= 0) {
            // Update the financials to subtract spending
            Financials financials = new Financials();
            financials.subtractFromSpending(orderCost);
            System.out.println("Order " + orderId + " has been declined.");
        } else {
            System.out.println("Order ID not found or already processed.");
        }
    }
    
}
