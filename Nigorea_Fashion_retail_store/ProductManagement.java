import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManagement {
    private final Map<String, Product> products;
    private static final String PRODUCTS_FILE = "products.txt";

    public ProductManagement() {
        this.products = new HashMap<>();
        loadProductsFromFile();
    }

    // Add a product and save it to the file
    public void addProduct(String id, String name, String description, double price, int stock) {
        Product product = new Product(name, description, price, 0.0, id, stock);
        products.put(id, product);
        saveProductsToFile();
    }

    // View all products with average review ratings
public void viewAllProducts(ReviewSystem reviewSystem) {
    if (products.isEmpty()) {
        System.out.println("No products available.");
        return;
    }

    System.out.printf("%-10s %-20s %-10s %-10s %-10s%n", "Product ID", "Product Name", "Price", "Stock", "Avg Rating");
    System.out.println("-------------------------------------------------------------");

    for (Product product : products.values()) {
        String productId = product.getProductID();
        double averageRating = reviewSystem.getAverageRating(productId); // Fetch average rating
        System.out.printf("%-10s %-20s $%-9.2f %-10d %-10.2f%n",
                productId, product.getName(), product.getPrice(), product.getStock(), averageRating);
    }
}

    // Update a product and save changes to the file
    public boolean updateProduct(String id, String name, String description, double price, int stock) {
        Product product = products.get(id);
        if (product != null) {
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            saveProductsToFile();
            return true;
        }
        return false;
    }

    // Load products from the text file
    private void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String id = parts[0];
                    String name = parts[1];
                    String description = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int stock = Integer.parseInt(parts[4]);
                    products.put(id, new Product(name, description, price, 0.0, id, stock));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Products file not found. Starting with an empty product list.");
        } catch (IOException e) {
            System.err.println("Error reading products file: " + e.getMessage());
        }
    }

    // Save products to the text file
    private void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            for (Product product : products.values()) {
                writer.write(product.getProductID() + "|" +
                             product.getName() + "|" +
                             product.getDescription() + "|" +
                             product.getPrice() + "|" +
                             product.getStock());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving products to file: " + e.getMessage());
        }
    }

    public String getProductName(String productId) {
        Product product = products.get(productId);
        return product != null ? product.getName() : null;
    }

    // Get a product by its ID
    public Product getProductById(String productId) {
        return products.get(productId);
    }

    // Get a product by its name
    public Product getProductByName(String productName) {
        for (Product product : products.values()) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }   
        return null;
    }

    public boolean deleteProduct(String productId) {
        if (products.remove(productId) != null) {
            saveProductsToFile(); // Persist changes
            return true;
        }
        return false;
    }

    public int getTotalProducts() {
        return products.size();
    }
    
    // Get products below a certain stock threshold
public Map<String, Product> getLowStockProducts(int threshold) {
    Map<String, Product> lowStockProducts = new HashMap<>();
    for (Product product : products.values()) {
        if (product.getStock() < threshold) {
            lowStockProducts.put(product.getProductID(), product);
        }
    }
    return lowStockProducts;
}

// Save supplier orders to a text file
public void createSupplierOrder(SupplierOrder order, String productId, int quantity) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier_orders.txt", true))) {
        writer.write(order.getOrderID() + "|" + order.getOrderDate() + "|" + order.getOrderStatus() + "|" +
                     order.getTotalCost() + "|" + productId + "|" + quantity);
        writer.newLine();
    } catch (IOException e) {
        System.err.println("Error saving supplier order: " + e.getMessage());
    }
}


// Update supplier order status
public boolean updateOrderStatus(String orderId, String newStatus, String eta) {
    List<String> updatedLines = new ArrayList<>();
    boolean found = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("supplier_orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(orderId)) {
                updatedLines.add(orderId + "|" + parts[1] + "|" + newStatus + "|" + parts[3] + "|" + eta);
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading supplier orders file: " + e.getMessage());
        return false;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier_orders.txt"))) {
        for (String line : updatedLines) {
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error updating supplier orders file: " + e.getMessage());
        return false;
    }

    return found;
}


public void viewPendingSupplierOrders() {
    try (BufferedReader reader = new BufferedReader(new FileReader("supplier_orders.txt"))) {
        String line;
        boolean hasPending = false;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[2].equalsIgnoreCase("Pending")) {
                System.out.println("Order ID: " + parts[0] + ", Product ID: " + parts[4] +
                                   ", Quantity: " + parts[5] + ", Total Cost: $" + parts[3]);
                hasPending = true;
            }
        }
        if (!hasPending) {
            System.out.println("No pending orders found.");
        }
    } catch (IOException e) {
        System.err.println("Error reading supplier orders: " + e.getMessage());
    }
}

public boolean fulfillSupplierOrder(String orderId) {
    List<String> updatedLines = new ArrayList<>();
    boolean found = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("supplier_orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(orderId) && parts[2].equalsIgnoreCase("Pending")) {
                // Update product stock
                String productId = parts[4];
                int quantity = Integer.parseInt(parts[5]);
                Product product = products.get(productId);
                if (product != null) {
                    product.setStock(product.getStock() + quantity);
                    saveProductsToFile(); // Update products file
                }
                updatedLines.add(orderId + "|" + parts[1] + "|Fulfilled|" + parts[3] + "|" + parts[4] + "|" + parts[5]);
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading supplier orders file: " + e.getMessage());
        return false;
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier_orders.txt"))) {
        for (String line : updatedLines) {
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error updating supplier orders file: " + e.getMessage());
        return false;
    }

    return found;
}

// public boolean declineSupplierOrder(String orderId) {
//     List<String> updatedLines = new ArrayList<>();
//     boolean found = false;

//     try (BufferedReader reader = new BufferedReader(new FileReader("supplier_orders.txt"))) {
//         String line;
//         while ((line = reader.readLine()) != null) {
//             String[] parts = line.split("\\|");
//             if (parts[0].equals(orderId) && parts[2].equalsIgnoreCase("Pending")) {
//                 updatedLines.add(orderId + "|" + parts[1] + "|Declined|" + parts[3] + "|" + parts[4] + "|" + parts[5]);
//                 found = true;
//             } else {
//                 updatedLines.add(line);
//             }
//         }
//     } catch (IOException e) {
//         System.err.println("Error reading supplier orders file: " + e.getMessage());
//         return false;
//     }

//     try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier_orders.txt"))) {
//         for (String line : updatedLines) {
//             writer.write(line);
//             writer.newLine();
//         }
//     } catch (IOException e) {
//         System.err.println("Error updating supplier orders file: " + e.getMessage());
//         return false;
//     }

//     return found;
// }

public double declineSupplierOrder(String orderId) {
    List<String> updatedLines = new ArrayList<>();
    double orderCost = -1;
    boolean found = false;

    try (BufferedReader reader = new BufferedReader(new FileReader("supplier_orders.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts[0].equals(orderId) && parts[2].equalsIgnoreCase("Pending")) {
                // Mark the order as declined
                updatedLines.add(orderId + "|" + parts[1] + "|Declined|" + parts[3] + "|" + parts[4] + "|" + parts[5]);
                orderCost = Double.parseDouble(parts[3]); // Extract the order cost
                found = true;
            } else {
                updatedLines.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading supplier orders file: " + e.getMessage());
    }

    if (found) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier_orders.txt"))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating supplier orders file: " + e.getMessage());
        }
    }

    return found ? orderCost : -1;
}



public boolean updateStock(String productId, int quantityChange) {
    Product product = products.get(productId);
    if (product != null) {
        int newStock = product.getStock() + quantityChange;
        if (newStock < 0) {
            System.out.println("Error: Insufficient stock for product ID " + productId);
            return false;
        }
        product.setStock(newStock);
        saveProductsToFile();
        return true;
    }
    System.out.println("Error: Product ID " + productId + " not found.");
    return false;
}

    
}
