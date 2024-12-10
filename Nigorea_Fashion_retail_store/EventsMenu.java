import java.util.Scanner;

public class EventsMenu {
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
                    handleFashionShows();
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

    private void handleFashionShows() {
        System.out.println("\n=== Fashion Shows ===");
        System.out.println("Join us for our exclusive fashion shows featuring the latest trends.");
        // Additional functionality can go here
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
