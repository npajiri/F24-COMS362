import java.util.Date;
import java.util.List;

public class DetailedReport {
    private Report reportMetadata;
    private double totalRevenue;
    private int totalUnitsSold;
    private String topSellingProduct;
    private List<String> details;

    // New fields for worker performance reports
    private String title;         // Title of the report
    private String startDate;     // Start date for the report
    private String endDate;       // End date for the report

    public DetailedReport(Report reportMetadata, List<String> details) {
        this.reportMetadata = reportMetadata;
        this.details = details;
    }

    public DetailedReport(String title, String startDate, String endDate, List<String> details) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.details = details;
    }

    public Report getReportMetadata() {
        return reportMetadata;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalUnitsSold() {
        return totalUnitsSold;
    }

    public void setTotalUnitsSold(int totalUnitsSold) {
        this.totalUnitsSold = totalUnitsSold;
    }

    public String getTopSellingProduct() {
        return topSellingProduct;
    }

    public void setTopSellingProduct(String topSellingProduct) {
        this.topSellingProduct = topSellingProduct;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    // Overridden toString to include worker performance reports
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // If worker performance fields are populated, format the worker report
        if (title != null && startDate != null && endDate != null) {
            sb.append("=== ").append(title).append(" ===\n");
            sb.append("Date Range: ").append(startDate).append(" to ").append(endDate).append("\n");
            sb.append("Details:\n");
            for (String detail : details) {
                sb.append("- ").append(detail).append("\n");
            }
        } else {
            // Fallback to original formatting
            sb.append("=== ").append(reportMetadata.getReportType()).append(" Report ===\n");
            sb.append("Report ID: ").append(reportMetadata.getReportID()).append("\n");
            sb.append("Date Generated: ").append(reportMetadata.getDateGenerated()).append("\n");
            sb.append("Details:\n");
            for (String detail : details) {
                sb.append("- ").append(detail).append("\n");
            }
        }
        return sb.toString();
    }
}
