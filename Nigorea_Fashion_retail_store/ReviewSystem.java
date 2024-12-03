// import java.io.*;
// import java.util.*;

// class ReviewSystem {
//     private static final String REVIEWS_FILE = "reviews.txt";

//     // Represents a review for a product
//     static class Review {
//         String productId;
//         String productName;
//         String reviewText;
//         int rating;

//         public Review(String productId, String productName, String reviewText, int rating) {
//             this.productId = productId;
//             this.productName = productName;
//             this.reviewText = reviewText;
//             this.rating = rating;
//         }

//         @Override
//         public String toString() {
//             return "Product Name: " + productName + ", Product ID: " + productId +
//                     ", Rating: " + rating + ", Review: " + reviewText;
//         }
//     }

//     // Write a review to the file
//     public void writeReview(String productId, String reviewText, int rating) {
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEWS_FILE, true))) {
//             writer.write(productId + "|" + rating + "|" + reviewText);
//             writer.newLine();
//             System.out.println("Review saved successfully.");
//         } catch (IOException e) {
//             System.err.println("Error saving review: " + e.getMessage());
//         }
//     }

//     // Get all reviews for a specific product
//     public List<Review> getProductReviews(String productId) {
//         List<Review> reviews = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] parts = line.split("\\|");
//                 if (parts[0].equals(productId)) {
//                     reviews.add(new Review(parts[0], parts[1], parts[3], Integer.parseInt(parts[2])));
//                 }
//             }
//         } catch (IOException e) {
//             System.err.println("Error reading reviews: " + e.getMessage());
//         }
//         return reviews;
//     }

//     // List all products reviewed
//     public Set<String> listReviewedProducts() {
//         Set<String> productIds = new HashSet<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] parts = line.split("\\|");
//                 productIds.add(parts[0] + " (" + parts[1] + ")");
//             }
//         } catch (IOException e) {
//             System.err.println("Error reading reviews: " + e.getMessage());
//         }
//         return productIds;
//     }

//     // Display products along with their reviews
//     public void displayProductReviews() {
//         Set<String> reviewedProducts = listReviewedProducts();
//         for (String product : reviewedProducts) {
//             System.out.println("Reviews for Product: " + product);
//             String productId = product.split(" ")[0]; // Extract productId from the string
//             List<Review> reviews = getProductReviews(productId);
//             for (Review review : reviews) {
//                 System.out.println("  - " + review);
//             }
//         }
//     }
// }


import java.io.*;
import java.util.*;

class ReviewSystem {
    private static final String REVIEWS_FILE = "reviews.txt";
    private final ProductManagement productManagement;

    public ReviewSystem() {
        this.productManagement = new ProductManagement();
    }

    // Represents a review for a product
    static class Review {
        String productId;
        String productName;
        String reviewText;
        int rating;

        public Review(String productId, String productName, String reviewText, int rating) {
            this.productId = productId;
            this.productName = productName;
            this.reviewText = reviewText;
            this.rating = rating;
        }

        // Getter for rating
    public int getRating() {
        return rating;
    }

        @Override
        public String toString() {
            return "Product Name: " + productName + ", Product ID: " + productId +
                    ", Rating: " + rating + ", Review: " + reviewText;
        }
    }

    // Write a review to the file
    public void writeReview(String productId, String reviewText, int rating) {
        // Fetch product name from ProductManagement
        String productName = productManagement.getProductName(productId);
        if (productName == null) {
            System.out.println("Invalid Product ID. Review not saved.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEWS_FILE, true))) {
            writer.write(productId + "|" + productName + "|" + rating + "|" + reviewText);
            writer.newLine();
            System.out.println("Review saved successfully for product: " + productName);
        } catch (IOException e) {
            System.err.println("Error saving review: " + e.getMessage());
        }
    }

    // Get all reviews for a specific product
    public List<Review> getProductReviews(String productId) {
        List<Review> reviews = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(productId)) {
                    reviews.add(new Review(parts[0], parts[1], parts[3], Integer.parseInt(parts[2])));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading reviews: " + e.getMessage());
        }
        return reviews;
    }

    // List all products reviewed
    public Set<String> listReviewedProducts() {
        Set<String> productIds = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                productIds.add(parts[0] + " (" + parts[1] + ")");
            }
        } catch (IOException e) {
            System.err.println("Error reading reviews: " + e.getMessage());
        }
        return productIds;
    }

    // Display products along with their reviews
    public void displayProductReviews() {
        Set<String> reviewedProducts = listReviewedProducts();
        for (String product : reviewedProducts) {
            System.out.println("Reviews for Product: " + product);
            String productId = product.split(" ")[0]; // Extract productId from the string
            List<Review> reviews = getProductReviews(productId);
            for (Review review : reviews) {
                System.out.println("  - " + review);
            }
        }
    }

    public boolean deleteReviewsForProduct(String productId) {
        boolean found = false;
        List<String> updatedLines = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(productId + "|")) {
                    updatedLines.add(line);
                } else {
                    found = true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading reviews file: " + e.getMessage());
            return false;
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEWS_FILE))) {
            for (String line : updatedLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to reviews file: " + e.getMessage());
            return false;
        }
    
        return found;
    }

    
    public int getTotalReviews() {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            while (reader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.err.println("Error reading reviews file: " + e.getMessage());
        }
        return count;
    }

    public double getAverageRating(String productId) {
        List<Review> reviews = getProductReviews(productId);
        if (reviews.isEmpty()) {
            return 0.0; // No reviews, default to 0.0
        }
    
        double totalRating = 0.0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
    
        return totalRating / reviews.size(); // Calculate average
    }
    
    
}
