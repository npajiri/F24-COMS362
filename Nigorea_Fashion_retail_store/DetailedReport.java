import java.util.Date;
import java.util.List;
import java.util.Map;

public class DetailedReport {
    private Report reportMetadata;
    private double totalRevenue;
    private int totalUnitsSold;
    private String topSellingProduct;
    private List<String> details;

    public DetailedReport(Report reportMetadata, List<String> details) {
        this.reportMetadata = reportMetadata;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(reportMetadata.getReportType()).append(" Report ===\n");
        sb.append("Report ID: ").append(reportMetadata.getReportID()).append("\n");
        sb.append("Date Generated: ").append(reportMetadata.getDateGenerated()).append("\n");
        sb.append("Details:\n");
        for (String detail : details) {
            sb.append("- ").append(detail).append("\n");
        }
        return sb.toString();
    }
}
