import java.util.Date;


public class Report {
     private String reportID;
    private String reportType;
    private Date dateGenerated;

    public Report(String reportID, String reportType, Date dateGenerated) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.dateGenerated = dateGenerated;
    }

    public String getReportID() { return reportID; }
    public void setReportID(String reportID) { this.reportID = reportID; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public Date getDateGenerated() { return dateGenerated; }
    public void setDateGenerated(Date dateGenerated) { this.dateGenerated = dateGenerated; }
}
