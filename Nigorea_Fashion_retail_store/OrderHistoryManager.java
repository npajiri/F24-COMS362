import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderHistoryManager {
    // Retrieve order history for a specific customer from a specific file
    public List<Order> getOrderHistory(String customerId, String ordersFile) {
        List<Order> orderHistory = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ordersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String orderID = parts[0];
                    Date orderDate = parseDate(parts[1]);
                    double totalCost = Double.parseDouble(parts[2]);
                    String shippingStatus = parts[3];
                    List<String> products = (parts.length > 4) ? Arrays.asList(parts[4].split(",")) : new ArrayList<>();
    
                    // Add the order directly to the history list
                    orderHistory.add(new Order(orderDate, totalCost, shippingStatus, orderID, products));
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving order history: " + e.getMessage());
        }
        return orderHistory;
    }

    // Display detailed information about a specific order
    public void displayOrderDetails(Order order) {
        System.out.println("\n=== Order Details ===");
        System.out.printf("Order ID: %s%n", order.getOrderID());
        System.out.printf("Date: %s%n", order.getOrderDate());
        System.out.printf("Total Cost: $%.2f%n", order.getTotalCost());
        System.out.printf("Shipping Status: %s%n", order.getShippingStatus());
        System.out.println("Itemized Purchases:");
        if (order.getProducts().isEmpty()) {
            System.out.println("  No products found for this order.");
        } else {
            for (String productId : order.getProducts()) {
                System.out.printf("  - Product ID: %s%n", productId);
            }
        }
        System.out.println("Shipping and Tracking: Not yet implemented"); // Placeholder
        System.out.println("Payment Details: Not yet implemented"); // Placeholder
    }

    // Apply filters to the order list
    public List<Order> applyFilters(List<Order> orders, int filterChoice, Scanner scanner) {
        switch (filterChoice) {
            case 1:
                System.out.print("Enter start date (yyyy-MM-dd): ");
                Date startDate = parseDate(scanner.nextLine());
                System.out.print("Enter end date (yyyy-MM-dd): ");
                Date endDate = parseDate(scanner.nextLine());
                return filterByDateRange(orders, startDate, endDate);
            case 2:
                System.out.print("Enter order status (e.g., Shipped, Pending): ");
                String status = scanner.nextLine();
                return filterByStatus(orders, status);
            case 3:
                System.out.print("Enter minimum price: ");
                double minPrice = scanner.nextDouble();
                System.out.print("Enter maximum price: ");
                double maxPrice = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                return filterByPriceRange(orders, minPrice, maxPrice);
            default:
                return orders;
        }
    }

    // Sort orders based on customer choice
    public List<Order> sortOrders(List<Order> orders, int sortChoice) {
        switch (sortChoice) {
            case 1:
                orders.sort(Comparator.comparing(Order::getOrderDate).reversed());
                break;
            case 2:
                orders.sort(Comparator.comparingDouble(Order::getTotalCost).reversed());
                break;
            case 3:
                orders.sort(Comparator.comparing(Order::getOrderID));
                break;
            default:
                System.out.println("Invalid sorting choice. Displaying unsorted.");
        }
        return orders;
    }

    // Filter orders by date range
    private List<Order> filterByDateRange(List<Order> orders, Date start, Date end) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getOrderDate() != null && !order.getOrderDate().before(start) && !order.getOrderDate().after(end)) {
                filtered.add(order);
            }
        }
        return filtered;
    }
    

    // Filter orders by status
    private List<Order> filterByStatus(List<Order> orders, String status) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getShippingStatus().equalsIgnoreCase(status)) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    // Filter orders by price range
    private List<Order> filterByPriceRange(List<Order> orders, double min, double max) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getTotalCost() >= min && order.getTotalCost() <= max) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    // Parse date from a string
    private Date parseDate(String dateString) {
    // Define the file's date format
    SimpleDateFormat fileDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    // Define the user's input date format
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    try {
        if (dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // If the date matches the input format (yyyy-MM-dd)
            return inputDateFormat.parse(dateString);
        } else {
            // Parse using the file's format
            return fileDateFormat.parse(dateString);
        }
    } catch (ParseException e) {
        System.err.println("Invalid date format: " + dateString);
        return null;
        }
    }

    public List<Order> searchOrders(List<Order> orders, String keyword) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            // Check if the Order ID contains the keyword (case-insensitive)
            if (order.getOrderID().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(order);
            }
        }
        return filtered;
    }
    
}
