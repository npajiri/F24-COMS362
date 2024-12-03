import java.util.Date;
import java.util.List;

// Customer class
class Customer {
    private String customerID;
    private String name;
    private String email;
    private List<Order> orderHistory;

    public Customer(String customerID, String name, String email) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
    }

    public String getCustomerID() { return customerID; }
    public void setCustomerID(String customerID) { this.customerID = customerID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Order> getOrderHistory() { return orderHistory; }
    public void setOrderHistory(List<Order> orderHistory) { this.orderHistory = orderHistory; }
}

// Product class
class Product {
    private String name;
    private String description;
    private double price;
    private double rating;
    private String productID;
    private int stock;

    public Product(String name, String description, double price, double rating, String productID, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.productID = productID;
        this.stock = stock;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return "Product ID: " + productID + ", Name: " + name + ", Description: " + description +
               ", Price: $" + price + ", Stock: " + stock;
    }
}

// Inventory class
class Inventory {
    private int stockLevel;

    public Inventory(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getStockLevel() { return stockLevel; }
    public void setStockLevel(int stockLevel) { this.stockLevel = stockLevel; }
}

// Supplier class
class Supplier {
    private String companyName;
    private String contact;
    private String supplierID;

    public Supplier(String companyName, String contact, String supplierID) {
        this.companyName = companyName;
        this.contact = contact;
        this.supplierID = supplierID;
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getSupplierID() { return supplierID; }
    public void setSupplierID(String supplierID) { this.supplierID = supplierID; }
}

// SupplierOrder class
class SupplierOrder {
    private Date orderDate;
    private String orderStatus;
    private double totalCost;
    private String orderID;

    public SupplierOrder(Date orderDate, String orderStatus, double totalCost, String orderID) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalCost = totalCost;
        this.orderID = orderID;
    }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getOrderID() { return orderID; }
    public void setOrderID(String orderID) { this.orderID = orderID; }
}

// SupplierContract class
class SupplierContract {
    private String contractID;
    private String terms;
    private Date startDate;
    private Date endDate;

    public SupplierContract(String contractID, String terms, Date startDate, Date endDate) {
        this.contractID = contractID;
        this.terms = terms;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getContractID() { return contractID; }
    public void setContractID(String contractID) { this.contractID = contractID; }

    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}

// Shipping class
class Shipping {
    private String trackingNumber;
    private String shippingID;
    private Date deliveryETA;

    public Shipping(String trackingNumber, String shippingID, Date deliveryETA) {
        this.trackingNumber = trackingNumber;
        this.shippingID = shippingID;
        this.deliveryETA = deliveryETA;
    }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getShippingID() { return shippingID; }
    public void setShippingID(String shippingID) { this.shippingID = shippingID; }

    public Date getDeliveryETA() { return deliveryETA; }
    public void setDeliveryETA(Date deliveryETA) { this.deliveryETA = deliveryETA; }
}

// Customs class
class Customs {
    private double customsFee;
    private double importTax;

    public Customs(double customsFee, double importTax) {
        this.customsFee = customsFee;
        this.importTax = importTax;
    }

    public double getCustomsFee() { return customsFee; }
    public void setCustomsFee(double customsFee) { this.customsFee = customsFee; }

    public double getImportTax() { return importTax; }
    public void setImportTax(double importTax) { this.importTax = importTax; }
}

// Return class
class Return {
    private Date returnDate;
    private String returnID;
    private double refundAmount;
    private String returnStatus;

    public Return(Date returnDate, String returnID, double refundAmount, String returnStatus) {
        this.returnDate = returnDate;
        this.returnID = returnID;
        this.refundAmount = refundAmount;
        this.returnStatus = returnStatus;
    }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public String getReturnID() { return returnID; }
    public void setReturnID(String returnID) { this.returnID = returnID; }

    public double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }

    public String getReturnStatus() { return returnStatus; }
    public void setReturnStatus(String returnStatus) { this.returnStatus = returnStatus; }
}

// CustomerService class
class CustomerService {
    private String agentName;
    private String agentID;

    public CustomerService(String agentName, String agentID) {
        this.agentName = agentName;
        this.agentID = agentID;
    }

    public String getAgentName() { return agentName; }
    public void setAgentName(String agentName) { this.agentName = agentName; }

    public String getAgentID() { return agentID; }
    public void setAgentID(String agentID) { this.agentID = agentID; }
}

// Wishlist class
class Wishlist {
    private String wishlistID;
    private List<Product> products;

    public Wishlist(String wishlistID, List<Product> products) {
        this.wishlistID = wishlistID;
        this.products = products;
    }

    public String getWishlistID() { return wishlistID; }
    public void setWishlistID(String wishlistID) { this.wishlistID = wishlistID; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}


