import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();
        ProductContainer productContainer = loadProductContainer();
        boolean autoMode = isAutoMode(args);

        if (!mode.equals("-auto")) {
            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Add a new product");
                System.out.println("2. View list of products");
                System.out.println("3. Find valid products");
                System.out.println("4. Exit program");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1:
                        addProduct(scanner, productContainer);
                        break;

                    case 2:
                        viewProducts(productContainer);
                        break;

                    case 3:
                        findValidProducts(productContainer);
                        break;

                    case 4:
                        saveProductContainer(productContainer);
                        System.out.println("Program terminated.");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            }
        } else {
            // Auto mode (for testing)
            addSampleProducts(productContainer);
            viewProducts(productContainer);
            findValidProducts(productContainer);
            saveProductContainer(productContainer);
            System.out.println("Auto mode execution completed.");
        }
    }

    private static void addProduct(Scanner scanner, ProductContainer productContainer) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter unit: ");
        String unit = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter price: ");
        int price = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer
        System.out.print("Enter arrival date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date arrivalDate = parseDate(dateStr);
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Product newProduct = new Product(name, unit, quantity, price, arrivalDate, description);
        productContainer.addProduct(newProduct);
        System.out.println("Product added!");
    }

    private static void viewProducts(ProductContainer productContainer) {
        System.out.println("List of products:");
        int index = 0;
        for (Product product : productContainer) {
            System.out.println(index + ". " + product);
            index++;
        }
    }

    private static void findValidProducts(ProductContainer productContainer) {
        List<Product> validProducts = productContainer.findValidProducts();
        System.out.println("Valid products:");
        for (Product product : validProducts) {
            System.out.println(product);
        }
    }

    private static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.err.println("Error parsing date. Using current date.");
            return new Date();
        }
    }

    private static ProductContainer loadProductContainer() {
        ProductContainer container;
        File file = new File("products.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                container = (ProductContainer) ois.readObject();
                System.out.println("Data loaded successfully from products.dat");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data from products.dat. Starting with an empty container.");
                container = new ProductContainer();
            }
        } else {
            System.out.println("products.dat not found. Starting with an empty container.");
            container = new ProductContainer();
        }
        return container;
    }

    private static void saveProductContainer(ProductContainer productContainer) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("products.dat"))) {
            oos.writeObject(productContainer);
            System.out.println("Data successfully saved to file products.dat");
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }

    private static boolean isAutoMode(String[] args) {
        return args.length > 0 && args[0].equals("-auto");
    }

    private static void addSampleProducts(ProductContainer productContainer) {
        // Add sample products for testing in auto mode
        productContainer.addProduct(new Product(
                "Milk",
                "liter",
                2,
                1,
                new Date(),
                "Manufacturing Date: 2023-01-01, Expiration Term: +7 days"
        ));

        productContainer.addProduct(new Product(
                "Bread",
                "piece",
                5,
                2,
                new Date(),
                "Manufacturing Date: 2023-01-02, Expiration Term: +14 days"
        ));

        productContainer.addProduct(new Product(
                "Eggs",
                "dozen",
                3,
                3,
                new Date(),
                "Manufacturing Date: 2023-01-03, Expiration Term: +30 days"
        ));
    }
}
