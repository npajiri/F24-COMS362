import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

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

    public Map<String, String> loadWorkerRoles() {
    Map<String, String> workerRoles = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("workers.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|"); // Split by '|'
            if (parts.length >= 3) { // Ensure sufficient columns exist
                String workerId = parts[0]; // WorkerID
                String role = parts[2];     // Role
                workerRoles.put(workerId, role); // Map WorkerID to Role
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading workers.txt: " + e.getMessage());
    }
    return workerRoles;
}


    public DetailedReport generateWorkerPerformanceReport(String startDate, String endDate, String roleFilter, String sortBy) {
        PerformanceDataHandler performanceDataHandler = new PerformanceDataHandler();
        List<Map<String, Object>> performanceData = new ArrayList<>(performanceDataHandler.getWorkerPerformanceData(startDate, endDate));
        Map<String, String> workerRoles = loadWorkerRoles(); // Load worker roles from workers.txt
    
        if (performanceData.isEmpty()) {
            return new DetailedReport("Worker Performance Report", startDate, endDate, List.of("No performance data found."));
        }
    
        // Filter by role using workers.txt
        if (roleFilter != null) {
            performanceData = performanceData.stream()
                .filter(record -> {
                    String workerId = (String) record.get("workerId");
                    String role = workerRoles.get(workerId);
                    return role != null && role.equalsIgnoreCase(roleFilter);
                })
                .toList();
            performanceData = new ArrayList<>(performanceData); // Convert to mutable
        }
    
        // Aggregate data for each worker
        Map<String, WorkerPerformance> workerPerformanceMap = new HashMap<>();
        for (Map<String, Object> record : performanceData) {
            String workerName = (String) record.get("workerName");
            int hoursWorked = (int) record.get("hoursWorked");
            double salesContribution = (double) record.get("salesContribution");
    
            WorkerPerformance performance = workerPerformanceMap.getOrDefault(workerName, new WorkerPerformance(workerName, 0, 0.0));
            performance.addPerformance(hoursWorked, salesContribution);
            workerPerformanceMap.put(workerName, performance);
        }
    
        // Sort by criteria if specified
        List<WorkerPerformance> workerPerformances = new ArrayList<>(workerPerformanceMap.values());
        if ("hours".equals(sortBy)) {
            workerPerformances.sort(Comparator.comparingInt(WorkerPerformance::getTotalHoursWorked).reversed());
        } else if ("sales".equals(sortBy)) {
            workerPerformances.sort(Comparator.comparingDouble(WorkerPerformance::getTotalSalesContribution).reversed());
        }
    
        // Generate report details
        List<String> reportDetails = workerPerformances.stream()
            .map(WorkerPerformance::toString)
            .toList(); // Returns an immutable list
        reportDetails = new ArrayList<>(reportDetails); // Convert to mutable for further operations
    
        // Add summary
        int totalWorkers = workerPerformances.size();
        int totalHours = workerPerformances.stream().mapToInt(WorkerPerformance::getTotalHoursWorked).sum();
        double totalSales = workerPerformances.stream().mapToDouble(WorkerPerformance::getTotalSalesContribution).sum();
        reportDetails.add("");
        reportDetails.add(String.format("Total Workers: %d", totalWorkers));
        reportDetails.add(String.format("Total Hours Worked: %d", totalHours));
        reportDetails.add(String.format("Total Sales Contributions: $%.2f", totalSales));
        reportDetails.add(String.format("Average Hours Worked: %.2f", totalWorkers > 0 ? (double) totalHours / totalWorkers : 0));
        reportDetails.add(String.format("Average Sales Contributions: $%.2f", totalWorkers > 0 ? totalSales / totalWorkers : 0));
    
        return new DetailedReport("Worker Performance Report", startDate, endDate, reportDetails);
    }
    
    

    
    
}
    