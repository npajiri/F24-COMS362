import java.io.*;
import java.util.*;

public class WorkerManagementSystem {
    private static final String WORKERS_FILE = "workers.txt"; // File to store worker data

    // Worker class representing a worker's contract details
    public static class Worker {
        private String workerId;
        private String name;
        private String contractType; // Full-time, Part-time, Contract
        private String position; 
        private double salary;
        private int hoursPerWeek;
        private String startDate;
        private String endDate;

        public Worker(String workerId, String name, String position, String contractType, double salary, int hoursPerWeek, String startDate, String endDate) {
            this.workerId = workerId;
            this.name = name;
            this.position = position;
            this.contractType = contractType;
            this.salary = salary;
            this.hoursPerWeek = hoursPerWeek;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getWorkerId() {
            return workerId;
        }
    
        public String getName() {
            return name;
        }
    
        public String getPosition() {
            return position;
        }
    
        public String getContractType() {
            return contractType;
        }
    
        public double getSalary() {
            return salary;
        }
    
        public int getHoursPerWeek() {
            return hoursPerWeek;
        }
    
        public String getStartDate() {
            return startDate;
        }
    
        public String getEndDate() {
            return endDate;
        }

        @Override
        public String toString() {
            return workerId + "|" + name + "|" + position + "|" + contractType + "|" + salary + "|" + hoursPerWeek + "|" + startDate + "|" + endDate;
        }

        public static Worker fromString(String line) {
            String[] parts = line.split("\\|");
            if (parts.length != 8) { // Updated to 8 fields
                throw new IllegalArgumentException("Invalid data format in workers.txt");
            }
            String workerId = parts[0];
            String name = parts[1];
            String position = parts[2];
            String contractType = parts[3];
            double salary = Double.parseDouble(parts[4]);
            int hoursPerWeek = Integer.parseInt(parts[5]);
            String startDate = parts[6];
            String endDate = parts[7];
            return new Worker(workerId, name, position, contractType, salary, hoursPerWeek, startDate, endDate);
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

    public boolean removeWorker(String workerId) {
        List<Worker> workers = getAllWorkers();
        boolean found = false;
    
        // Filter out the worker with the specified ID
        List<Worker> updatedWorkers = new ArrayList<>();
        for (Worker worker : workers) {
            if (worker.getWorkerId().equals(workerId)) {
                found = true;
            } else {
                updatedWorkers.add(worker);
            }
        }
    
        if (found) {
            saveWorkersToFile(updatedWorkers);
        }
    
        return found; // Return true if the worker was found and removed, false otherwise
    }
    
    
}
