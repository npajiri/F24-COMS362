import java.io.*;
import java.util.*;

public class PerformanceDataHandler {
    private static final String PERFORMANCE_FILE = "performance.txt";

    // Method to fetch worker performance data based on a date range
    public List<Map<String, Object>> getWorkerPerformanceData(String startDate, String endDate) {
        List<Map<String, Object>> performanceData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PERFORMANCE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Parse the performance entry
                String workerId = parts[0];
                String workerName = parts[1];
                String date = parts[2];
                int hoursWorked = Integer.parseInt(parts[3]);
                double salesContribution = Double.parseDouble(parts[4]);

                // Check if the date falls within the range
                if (isDateInRange(date, startDate, endDate)) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("workerId", workerId);
                    record.put("workerName", workerName);
                    record.put("date", date);
                    record.put("hoursWorked", hoursWorked);
                    record.put("salesContribution", salesContribution);

                    performanceData.add(record);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Performance file not found. No data available.");
        } catch (IOException e) {
            System.err.println("Error reading performance file: " + e.getMessage());
        }

        return performanceData;
    }

    // Helper method to check if a date is within the range
    private boolean isDateInRange(String date, String startDate, String endDate) {
        return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
    }
}
