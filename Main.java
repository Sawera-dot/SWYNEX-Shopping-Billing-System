import java.sql.*;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static String url = "jdbc:mysql://localhost:3306/shopping_db";
    static String user = "root";
    static String password = "sawera@123";

    static Connection connect() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== SHOPPING BILLING SYSTEM ===");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Buy Product");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();

            if (choice == 1) addProduct();
            else if (choice == 2) viewProducts();
            else if (choice == 3) buyProduct();
            else if (choice == 4) {
                System.out.println("Thank you!");
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    static void addProduct() {
        try {
            System.out.print("ID: ");
            int id = sc.nextInt();

            System.out.print("Name: ");
            String name = sc.next();

            System.out.print("Price: ");
            double price = sc.nextDouble();

            System.out.print("Quantity: ");
            int quantity = sc.nextInt();

            if (id <= 0 || price <= 0 || quantity <= 0) {
                System.out.println("Invalid data!");
                return;
            }

            String sql = "INSERT INTO products VALUES (?, ?, ?, ?)";
            PreparedStatement ps = connect().prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);

            ps.executeUpdate();
            System.out.println("Product added!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewProducts() {
        try {
            ResultSet rs = connect()
                    .createStatement()
                    .executeQuery("SELECT * FROM products");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + "  " +
                                rs.getString("name") + "  Rs." +
                                rs.getDouble("price") + "  " +
                                rs.getInt("quantity")
                );
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void buyProduct() {
        try {
            System.out.print("Product ID: ");
            int id = sc.nextInt();

            System.out.print("Quantity: ");
            int qty = sc.nextInt();

            if (qty <= 0) {
                System.out.println("Invalid quantity!");
                return;
            }

            PreparedStatement ps = connect().prepareStatement(
                    "SELECT * FROM products WHERE id=?"
            );

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double total = rs.getDouble("price") * qty;

                System.out.println("\n===== BILL =====");
                System.out.println("Product: " + rs.getString("name"));
                System.out.println("Quantity: " + qty);
                System.out.println("Total: Rs." + total);
                System.out.println("================");
            } else {
                System.out.println("Product not found!");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}