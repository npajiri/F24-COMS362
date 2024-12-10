import java.io.*;
import java.util.*;

public class WorkerManagementSystem {
    private final String WORKERS_FILE = "workers.txt"; // File to store worker data

    // Worker class representing a worker's contract details
    public static class Worker {
        private String workerId;
        private String name;
        private String contractType; // Full-time, Part-time, Contract
        private double salary;
        private int hoursPerWeek;
        private String startDate;
        private String endDate;

        public Worker(String workerId, String name, String contractType, double salary, int hoursPerWeek, String startDate, String endDate) {
            this.workerId = workerId;
            this.name = name;
            this.contractType = contractType;
            this.salary = salary;
            this.hoursPerWeek = hoursPerWeek;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public String toString() {
            return workerId + "|" + name + "|" + contractType + "|" + salary + "|" + hoursPerWeek + "|" + startDate + "|" + endDate;
        }

        public static Worker fromString(String line) {
            String[] parts = line.split("\\|");
            return new Worker(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6]);
        }
    }

    // Retrieve a worker by ID
    public Worker getWorkerById(String workerId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(WORKERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Worker worker = Worker.fromString(line);
                if (worker.workerId.equals(workerId)) {
                    return worker;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading workers file: " + e.getMessage());
        }
        return null;
    }

    // Add or update a worker's contract
    public void addOrUpdateWorker(Worker worker) {
        List<Worker> workers = getAllWorkers();
        boolean updated = false;

        // Update if worker exists
        for (int i = 0; i < workers.size(); i++) {
            if (workers.get(i).workerId.equals(worker.workerId)) {
                workers.set(i, worker);
                updated = true;
                break;
            }
        }

        // Add if worker does not exist
        if (!updated) {
            workers.add(worker);
        }

        saveWorkersToFile(workers);
    }

    // Retrieve all workers
    public List<Worker> getAllWorkers() {
        List<Worker> workers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(WORKERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                workers.add(Worker.fromString(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Workers file not found. Starting fresh.");
        } catch (IOException e) {
            System.err.println("Error reading workers file: " + e.getMessage());
        }
        return workers;
    }

    // Save all workers to file
    private void saveWorkersToFile(List<Worker> workers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WORKERS_FILE))) {
            for (Worker worker : workers) {
                writer.write(worker.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving workers to file: " + e.getMessage());
        }
    }
}
