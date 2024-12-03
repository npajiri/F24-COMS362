import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private Date orderDate;            // Represents the order's date
    private double totalCost;          // Total cost of the order
    private String shippingStatus;     // Shipping status of the order
    private String orderID;            // Unique ID for the order
    private List<String> products;     // List of product IDs in the order

    // Constructor for basic orders (fashion_retail-style)
    public Order(Date orderDate, double totalCost, String shippingStatus, String orderID) {
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.shippingStatus = shippingStatus;
        this.orderID = orderID;
        this.products = new ArrayList<>(); 
    }

    // Constructor for detailed orders
    public Order(Date orderDate, double totalCost, String shippingStatus, String orderID, List<String> products) {
        this.orderDate = orderDate;
        this.totalCost = totalCost;
        this.shippingStatus = shippingStatus;
        this.orderID = orderID;
        this.products = products;
    }

    // Getter and Setter for orderDate
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    // Getter and Setter for totalCost
    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    // Getter and Setter for shippingStatus
    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    // Getter and Setter for orderID
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    // Getter and Setter for products
    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    // Helper to add a single product to the list
    public void addProduct(String productId) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(productId);
    }
}
