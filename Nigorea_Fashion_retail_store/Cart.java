import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private static final String CART_FILE = "carts.txt"; // File to store carts
    private final Map<String, Map<String, Integer>> carts; // Customer ID to (Product ID -> Quantity)
    private final Map<String, Double> totalCosts; // Customer ID to Total Cost

    public Cart() {
        this.carts = new HashMap<>();
        this.totalCosts = new HashMap<>();
        loadCartsFromFile();
    }

    public void addItem(String customerId, String productId, int quantity, double price) {
        // Retrieve or initialize the customer's cart
        Map<String, Integer> customerCart = carts.getOrDefault(customerId, new HashMap<>());
        customerCart.put(productId, customerCart.getOrDefault(productId, 0) + quantity);
        carts.put(customerId, customerCart);

        // Update total cost for the customer
        totalCosts.put(customerId, totalCosts.getOrDefault(customerId, 0.0) + quantity * price);

        saveCartsToFile();
    }

    public void removeItem(String customerId, String productId, int quantity, double price) {
        if (carts.containsKey(customerId)) {
            Map<String, Integer> customerCart = carts.get(customerId);
            if (customerCart.containsKey(productId)) {
                int currentQuantity = customerCart.get(productId);
                int newQuantity = Math.max(currentQuantity - quantity, 0);
                if (newQuantity == 0) {
                    customerCart.remove(productId);
                } else {
                    customerCart.put(productId, newQuantity);
                }

                // Update total cost for the customer
                totalCosts.put(customerId, totalCosts.getOrDefault(customerId, 0.0)
                        - Math.min(quantity, currentQuantity) * price);

                // Remove the customer cart if it's empty
                if (customerCart.isEmpty()) {
                    carts.remove(customerId);
                    totalCosts.remove(customerId);
                }
            }
            saveCartsToFile();
        }
    }

    public Map<String, Integer> getItems(String customerId) {
        return carts.getOrDefault(customerId, new HashMap<>());
    }

    public double getTotalCost(String customerId) {
        return totalCosts.getOrDefault(customerId, 0.0);
    }

    public void clearCart(String customerId) {
        carts.remove(customerId);
        totalCosts.remove(customerId);
        saveCartsToFile();
    }

    private void saveCartsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CART_FILE))) {
            for (Map.Entry<String, Map<String, Integer>> customerEntry : carts.entrySet()) {
                String customerId = customerEntry.getKey();
                Map<String, Integer> customerCart = customerEntry.getValue();
                double totalCost = totalCosts.getOrDefault(customerId, 0.0);

                StringBuilder cartLine = new StringBuilder(customerId + "|" + totalCost);
                for (Map.Entry<String, Integer> itemEntry : customerCart.entrySet()) {
                    cartLine.append("|").append(itemEntry.getKey()).append(":").append(itemEntry.getValue());
                }
                writer.write(cartLine.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving carts to file: " + e.getMessage());
        }
    }

    private void loadCartsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String customerId = parts[0];
                    double totalCost = Double.parseDouble(parts[1]);
                    Map<String, Integer> customerCart = new HashMap<>();

                    for (int i = 2; i < parts.length; i++) {
                        String[] itemParts = parts[i].split(":");
                        if (itemParts.length == 2) {
                            String productId = itemParts[0];
                            int quantity = Integer.parseInt(itemParts[1]);
                            customerCart.put(productId, quantity);
                        }
                    }

                    carts.put(customerId, customerCart);
                    totalCosts.put(customerId, totalCost);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cart file not found. Starting with empty carts.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading carts from file: " + e.getMessage());
        }
    }

    public int getQuantity(String customerId, String productId) {
        // TODO Auto-generated method stub
        int currentInCart = getItems(customerId).getOrDefault(productId, 0);
                return currentInCart;
    }
}




// import java.util.HashMap;
// import java.util.Map;

// public class Cart {
//     private final Map<String, Integer> items; // Product ID to Quantity mapping
//     private double totalCost;

//     public Cart() {
//         this.items = new HashMap<>();
//         this.totalCost = 0.0;
//     }

//     public void addItem(String productId, int quantity, double price) {
//         items.put(productId, items.getOrDefault(productId, 0) + quantity);
//         totalCost += quantity * price;
//     }

//     public void removeItem(String productId, int quantity, double price) {
//         if (items.containsKey(productId)) {
//             int currentQuantity = items.get(productId);
//             int newQuantity = Math.max(currentQuantity - quantity, 0);
//             if (newQuantity == 0) {
//                 items.remove(productId);
//             } else {
//                 items.put(productId, newQuantity);
//             }
//             totalCost -= Math.min(quantity, currentQuantity) * price;
//         }
//     }

//     public Map<String, Integer> getItems() {
//         return items;
//     }

//     public double getTotalCost() {
//         return totalCost;
//     }

//     public void clearCart() {
//         items.clear();
//         totalCost = 0.0;
//     }
// }
