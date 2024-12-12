import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ReportManager {
    private final Database database;

    public ReportManager(Database database) {
        this.database = database;
    }

    // Generate a detailed report
    public DetailedReport generateReport(String reportType, Map<String, String> parameters) {
        try {
            System.out.println("Retrieving data from the database...");
            List<Map<String, Object>> data = database.retrieveData(reportType, parameters);

            if (data.isEmpty()) {
                System.out.println("No data available for the selected parameters. Please adjust the parameters and try again.");
                return null;
            }

            System.out.println("Calculating metrics...");
            return calculateMetrics(reportType, parameters, data);

        } catch (DatabaseException e) {
            System.err.println("Error retrieving data: " + e.getMessage());
            System.out.println("Please try again later or contact support.");
        }
        return null;
    }

    // Calculate metrics and generate the detailed report
    private DetailedReport calculateMetrics(String reportType, Map<String, String> parameters, List<Map<String, Object>> data) {
        String reportID = "REP" + System.currentTimeMillis(); // Unique report ID
        Date dateGenerated = new Date();

        Report reportMetadata = new Report(reportID, reportType, dateGenerated);

        if (reportType.equalsIgnoreCase("sales")) {
            return generateSalesMetrics(reportMetadata, data);
        } else if (reportType.equalsIgnoreCase("inventory")) {
            return generateInventoryMetrics(reportMetadata, data);
        } else {
            throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }

    // Generate sales metrics
    private DetailedReport generateSalesMetrics(Report reportMetadata, List<Map<String, Object>> data) {
        double totalRevenue = 0;
        int totalUnitsSold = 0;
        String topSellingProduct = "N/A";

        Map<String, Integer> productSales = new java.util.HashMap<>();

        for (Map<String, Object> record : data) {
            double price = (double) record.get("price");
            int unitsSold = (int) record.get("unitsSold");
            String productName = (String) record.get("productName");

            totalRevenue += price * unitsSold;
            totalUnitsSold += unitsSold;

            // Track sales for each product to find the top-selling one
            productSales.put(productName, productSales.getOrDefault(productName, 0) + unitsSold);
        }

        // Determine the top-selling product
        if (!productSales.isEmpty()) {
            topSellingProduct = productSales.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("N/A");
        }

        // Prepare report details
        List<String> details = new ArrayList<>();
        details.add("Total Revenue: $" + String.format("%.2f", totalRevenue));
        details.add("Total Units Sold: " + totalUnitsSold);
        details.add("Top-Selling Product: " + topSellingProduct);

        return new DetailedReport(reportMetadata, details);
    }

    // Generate inventory metrics
    private DetailedReport generateInventoryMetrics(Report reportMetadata, List<Map<String, Object>> data) {
        List<String> details = new ArrayList<>();

        for (Map<String, Object> record : data) {
            String productName = (String) record.get("productName");
            int currentStock = (int) record.get("currentStock");
            double turnoverRate = (double) record.get("turnoverRate");

            details.add(String.format("Product: %s | Current Stock: %d | Turnover Rate: %.2f%%",
                    productName, currentStock, turnoverRate));
        }

        return new DetailedReport(reportMetadata, details);
    }
    
    public void exportReport(DetailedReport detailedReport) {
        System.out.println("Exporting report: " + detailedReport.getReportMetadata().getReportType());
        // File writing logic could be implemented here
    }

    public DetailedReport generateWorkerPerformanceReport(String startDate, String endDate) {
        PerformanceDataHandler performanceDataHandler = new PerformanceDataHandler();
        List<Map<String, Object>> performanceData = performanceDataHandler.getWorkerPerformanceData(startDate, endDate);
    
        if (performanceData.isEmpty()) {
            System.out.println("No performance data found for the specified date range.");
            return new DetailedReport("Worker Performance Report", startDate, endDate, new ArrayList<>());
        }
    
        // Create a report data structure
        List<String> reportDetails = new ArrayList<>();
        for (Map<String, Object> record : performanceData) {
            String detail = String.format(
                "Worker: %s, Hours Worked: %d, Sales Contributions: $%.2f",
                record.get("workerName"),
                record.get("hoursWorked"),
                record.get("salesContribution")
            );
            reportDetails.add(detail);
        }
    
        // Create and return a DetailedReport object
        return new DetailedReport("Worker Performance Report", startDate, endDate, reportDetails);
    }
    
    
}
    