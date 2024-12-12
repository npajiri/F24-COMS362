public class WorkerPerformance {
    private String workerName;
    private int totalHoursWorked; // This should be an int
    private double totalSalesContribution;

    public WorkerPerformance(String workerName, int totalHoursWorked, double totalSalesContribution) {
        this.workerName = workerName;
        this.totalHoursWorked = totalHoursWorked;
        this.totalSalesContribution = totalSalesContribution;
    }

    public int getTotalHoursWorked() {
        return totalHoursWorked; // This must return an int
    }

    public double getTotalSalesContribution() {
        return totalSalesContribution;
    }

    public void addPerformance(int hoursWorked, double salesContribution) {
        this.totalHoursWorked += hoursWorked;
        this.totalSalesContribution += salesContribution;
    }

    @Override
    public String toString() {
        return String.format("Worker: %s, Total Hours Worked: %d, Total Sales Contributions: $%.2f",
                workerName, totalHoursWorked, totalSalesContribution);
    }
}
