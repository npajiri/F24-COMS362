import java.util.Scanner;

public class EmployeeMenu {
    private final ProductManagement productManagement;
    private final ReviewSystem reviewSystem;

    public EmployeeMenu() {
        this.productManagement = new ProductManagement();
        this.reviewSystem = new ReviewSystem();
    }

    public void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. Add a Product");
            System.out.println("2. View All Products");
            System.out.println("3. Update Product Details");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleAddProduct(scanner);
                    break;

                case 2:
                    handleViewAllProducts();
                    break;

                case 3:
                    handleUpdateProduct(scanner);
                    break;

                case 4:
                    System.out.println("Exiting to main menu...");
                    running = false; // Exit the loop to return to the main menu
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleAddProduct(Scanner scanner) {
        System.out.println("\n=== Add a Product ===");
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
        System.out.println("Product added successfully!");
    }

    private void handleViewAllProducts() {
        System.out.println("\n=== All Products ===");
        productManagement.viewAllProducts(null);
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
}
