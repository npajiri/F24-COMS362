import java.io.*;
import java.util.*;

public class Database {
    private static final String PRODUCTS_FILE = "products.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String REVIEWS_FILE = "reviews.txt";
    private static final String SALES_FILE = "sales.txt";
    private static final String INVENTORY_FILE = "inventory.txt";

    /*
     * Retrieve data based on report type and parameters
     */
    public List<Map<String, Object>> retrieveData(String reportType, Map<String, String> parameters) throws DatabaseException {
        try {
            switch (reportType.toLowerCase()) {
                case "sales":
                    return getSalesData(parameters);
                case "inventory":
                    return getInventoryData(parameters);
                default:
                    throw new DatabaseException("Unsupported report type: " + reportType);
            }
        } catch (IOException e) {
            throw new DatabaseException("Error accessing data files: " + e.getMessage(), e);
        }
    }

    /*
     * Retrieve sales data from sales.txt
     */
    private List<Map<String, Object>> getSalesData(Map<String, String> parameters) throws IOException {
        List<Map<String, Object>> salesData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Validate and populate fields
                String productName = parts.length > 0 ? parts[0] : "Unknown Product";
                Double price = parts.length > 1 ? Double.parseDouble(parts[1]) : 0.0;
                Integer unitsSold = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
                String date = parts.length > 3 ? parts[3] : "1970-01-01";

                Map<String, Object> record = new HashMap<>();
                record.put("productName", productName);
                record.put("price", price);
                record.put("unitsSold", unitsSold);
                record.put("date", date);

                // Filter data based on parameters
                if (matchesParameters(record, parameters)) {
                    salesData.add(record);
                }
            }
        }
        return salesData;
    }
    

    /*
     * Retrieve inventory data from inventory.txt
     */
    private List<Map<String, Object>> getInventoryData(Map<String, String> parameters) throws IOException {
        List<Map<String, Object>> inventoryData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Validate and populate fields
                String productName = parts.length > 0 ? parts[0] : "Unknown Product";
                Integer currentStock = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                Double turnoverRate = parts.length > 2 ? Double.parseDouble(parts[2]) : 0.0;

                Map<String, Object> record = new HashMap<>();
                record.put("productName", productName);
                record.put("currentStock", currentStock);
                record.put("turnoverRate", turnoverRate);

                // Filter data based on parameters
                if (matchesParameters(record, parameters)) {
                    inventoryData.add(record);
                }
            }
        }
        return inventoryData;
    }

     /*
     * Check if a record matches the given parameters
     */
    private boolean matchesParameters(Map<String, Object> record, Map<String, String> parameters) {
        // Example: Filter by date range for sales data
        if (parameters.containsKey("startDate") && parameters.containsKey("endDate")) {
            String recordDate = (String) record.get("date");
            if (recordDate != null) {
                String startDate = parameters.get("startDate");
                String endDate = parameters.get("endDate");
                if (recordDate.compareTo(startDate) < 0 || recordDate.compareTo(endDate) > 0) {
                    return false;
                }
            }
        }

        // Example: Filter by category or product name
        if (parameters.containsKey("category")) {
            String category = parameters.get("category");
            String productName = (String) record.get("productName");
            if (productName != null && !productName.contains(category)) {
                return false;
            }
        }

        return true;
    }

    /*
     * Retrieve review data
     */ 
    private List<Map<String, Object>> getReviewData(Map<String, String> parameters) throws IOException {
        List<Map<String, Object>> reviewData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("productId", parts[0]);
                    record.put("productName", parts[1]);
                    record.put("rating", Integer.parseInt(parts[2]));
                    record.put("reviewText", parts[3]);

                    // Example: Add filtering logic based on parameters (e.g., productId)
                    reviewData.add(record);
                }
            }
        }
        return reviewData;
    }

    /*
     * Save a record to a file
     */
    public void saveRecord(String reportType, Map<String, Object> record) throws DatabaseException {
        String targetFile;
        switch (reportType.toLowerCase()) {
            case "sales":
                targetFile = ORDERS_FILE;
                break;
            case "inventory":
                targetFile = PRODUCTS_FILE;
                break;
            case "reviews":
                targetFile = REVIEWS_FILE;
                break;
            default:
                throw new DatabaseException("Unsupported report type for saving: " + reportType);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true))) {
            writer.write(formatRecord(record));
            writer.newLine();
        } catch (IOException e) {
            throw new DatabaseException("Error saving record to " + targetFile + ": " + e.getMessage(), e);
        }
    }

    /*
     * Format a record for saving to a file
     */
    private String formatRecord(Map<String, Object> record) {
        StringBuilder sb = new StringBuilder();
        record.forEach((key, value) -> sb.append(value).append("|"));
        sb.deleteCharAt(sb.length() - 1); // Remove the trailing "|"
        return sb.toString();
    }
}
class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}