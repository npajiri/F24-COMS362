import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println("4. Handle Work Orders");
            System.out.println("0. Exit to Main Menu");
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
                    handleWorkOrders(scanner);
                    break;

                case 0:
                    System.out.println("Exiting to main menu...");
                    running = false; // Exit the loop to return to the main menu
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleWorkOrders(Scanner scanner) {
        System.out.println("\n=== Work Orders ===");
        
        while (true) {
            System.out.println("\n1. View Pending Orders");
            System.out.println("2. View Completed Orders");
            System.out.println("3. View All Orders");
            System.out.println("4. Mark Order as Completed");
            System.out.println("5. Return to Admin Menu");
            System.out.print("Enter your choice: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    viewOrdersByStatus("Pending");
                    break;
    
                case 2:
                    viewOrdersByStatus("Completed");
                    break;
    
                case 3:
                    viewAllOrders();
                    break;
    
                case 4:
                      System.out.print("Enter Registration ID to mark as completed: ");
                    String registrationId = scanner.nextLine();
                    markWorkOrderAsCompleted(registrationId);
                    break;
    
                case 5:
                    System.out.println("Returning to Admin Menu...");
                    return;
    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    } 

    // private void handleWorkOrders(Scanner scanner) {
    // System.out.println("\n=== Work Orders ===");
    // try (BufferedReader reader = new BufferedReader(new FileReader("work_orders.txt"))) {
    //     String line;
    //     while ((line = reader.readLine()) != null) {
    //         System.out.println(line);
    //     }
    // } catch (IOException e) {
    //     System.err.println("Error reading work orders: " + e.getMessage());
    // }

    // System.out.print("Enter Registration ID to mark as completed: ");
    // String registrationId = scanner.nextLine();

    // markWorkOrderAsCompleted(registrationId);
    // }

    private void viewOrdersByStatus(String status) {
        System.out.println("\n=== " + status + " Work Orders ===");
        try (BufferedReader reader = new BufferedReader(new FileReader("work_orders.txt"))) {
            String line;
            boolean hasOrders = false;
    
            while ((line = reader.readLine()) != null) {
                if (line.contains("Status: " + status)) {
                    System.out.println(line);
                    hasOrders = true;
                }
            }
    
            if (!hasOrders) {
                System.out.println("No " + status.toLowerCase() + " work orders found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading work orders: " + e.getMessage());
        }
    }

    private void viewAllOrders() {
        System.out.println("\n=== All Work Orders ===");
        try (BufferedReader reader = new BufferedReader(new FileReader("work_orders.txt"))) {
            String line;
            boolean hasOrders = false;
    
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                hasOrders = true;
            }
    
            if (!hasOrders) {
                System.out.println("No work orders found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading work orders: " + e.getMessage());
        }
    }

    // Mark work order as completed
    private void markWorkOrderAsCompleted(String registrationId) {
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("work_orders.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                if (line.contains("Registration ID: " + registrationId)) {
                    found = true;
                    int index = lines.size() - 1;
                    lines.set(index, lines.get(index).replace("Status: Pending", "Status: Completed"));
                }
            }

            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("work_orders.txt"))) {
                    for (String updatedLine : lines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                    System.out.println("Work order marked as completed.");
                } catch (IOException e) {
                    System.err.println("Error updating work orders: " + e.getMessage());
                }
            } else {
                System.out.println("Registration ID not found.");
            }
        } catch (IOException e) {
            System.err.println("Error reading work orders: " + e.getMessage());
        }
    }
// private void markWorkOrderAsCompleted(String registrationId) {
//     List<String> updatedOrders = new ArrayList<>();
//     boolean found = false;

//     try (BufferedReader reader = new BufferedReader(new FileReader("work_orders.txt"))) {
//         String line;
//         while ((line = reader.readLine()) != null) {
//             if (line.contains("Registration ID: " + registrationId)) {
//                 found = true;
//                 updatedOrders.add(line + " | Status: Completed");
//             } else {
//                 updatedOrders.add(line);
//             }
//         }
//     } catch (IOException e) {
//         System.err.println("Error reading work orders: " + e.getMessage());
//     }

//     if (found) {
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter("work_orders.txt"))) {
//             for (String updatedOrder : updatedOrders) {
//                 writer.write(updatedOrder);
//                 writer.newLine();
//             }
//             System.out.println("Work order marked as completed.");
//         } catch (IOException e) {
//             System.err.println("Error updating work orders: " + e.getMessage());
//         }
//     } else {
//         System.out.println("Registration ID not found.");
//     }
// }


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

        System.out.print("Enter Inventory to add product to (e.g., Main, Warehouse-A, Warehouse-B): ");
        String inventoryName = scanner.nextLine();

        productManagement.addProduct(productId, productName, description, price, stock, inventoryName);
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
