import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class EventsMenu {
    private final CustomerManagement customerManagement;

    public EventsMenu() {
        this.customerManagement = new CustomerManagement();
    }
    public void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Events Menu ===");
            System.out.println("1. Fashion Shows");
            System.out.println("2. Exclusive Launch Parties");
            System.out.println("3. Seasonal Sale Previews");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleFashionShows(scanner);
                    break;
                case 2:
                    handleLaunchParties();
                    break;
                case 3:
                    handleSalePreviews();
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleFashionShows(Scanner scanner) {
        System.out.println("\n=== Fashion Shows ===");
        System.out.print("Enter your Customer ID: ");
        String customerId = scanner.nextLine();

        // Validate Customer ID
        if (!customerManagement.customerExists(customerId)) {
            System.out.println("Customer ID not found. Please register in the customer menu.");
            return;
        }

        System.out.println("Welcome to our exclusive fashion shows!");
        System.out.println("The registration fee is $50.");
        System.out.print("Enter amount to pay: ");
        double payment = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        if (payment != 50.0) {
            System.out.println("Incorrect amount. Please try again.");
            return;
        }

        System.out.println("Registration successful! You are now registered for the fashion show.");
        String registrationId = "REG" + System.currentTimeMillis();
        System.out.println("Your Registration ID is: " + registrationId);

        System.out.print("Describe your desired outfit: ");
        String outfitDescription = scanner.nextLine();

        saveFashionShowRegistration(customerId, registrationId, outfitDescription);

        // Save the event order
        saveEventOrder(customerId, "Fashion Show", registrationId, outfitDescription, "Pending");
    }

    private void saveFashionShowRegistration(String customerId, String registrationId, String outfitDescription) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fashion_show_registrations.txt", true))) {
            writer.write(customerId + "|" + registrationId + "|" + outfitDescription);
            writer.newLine();
            System.out.println("Your registration has been saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving registration: " + e.getMessage());
        }
    }

    private void saveEventOrder(String customerId, String eventName, String eventOrderId, 
                            String clothingDescription, String shippingStatus) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("event_orders.txt", true))) {
        writer.write(customerId + "|" + eventName + "|" + eventOrderId + "|" 
                     + clothingDescription + "|" + shippingStatus);
        writer.newLine();
        System.out.println("Your event order has been saved successfully.");
    } catch (IOException e) {
        System.err.println("Error saving event order: " + e.getMessage());
    }
}


    private void handleLaunchParties() {
        System.out.println("\n=== Exclusive Launch Parties ===");
        System.out.println("Be the first to see our new collection launches at exclusive parties.");
        // Additional functionality can go here
    }

    private void handleSalePreviews() {
        System.out.println("\n=== Seasonal Sale Previews ===");
        System.out.println("Get early access to our seasonal sales and discounts.");
        // Additional functionality can go here
    }
}
