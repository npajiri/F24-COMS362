import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<String, Integer> items; // Product ID to Quantity mapping
    private double totalCost;

    public Cart() {
        this.items = new HashMap<>();
        this.totalCost = 0.0;
    }

    public void addItem(String productId, int quantity, double price) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
        totalCost += quantity * price;
    }

    public void removeItem(String productId, int quantity, double price) {
        if (items.containsKey(productId)) {
            int currentQuantity = items.get(productId);
            int newQuantity = Math.max(currentQuantity - quantity, 0);
            if (newQuantity == 0) {
                items.remove(productId);
            } else {
                items.put(productId, newQuantity);
            }
            totalCost -= Math.min(quantity, currentQuantity) * price;
        }
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void clearCart() {
        items.clear();
        totalCost = 0.0;
    }
}
