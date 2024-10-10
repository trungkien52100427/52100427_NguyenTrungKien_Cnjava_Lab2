package vn.edu.tdtu.javatech.lab2;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Program {
    private ProductDAO<Product, Long> productDAO;
    private BasicDataSource dataSource;

    public Program() {
        productDAO = new ProductDAO<>();
        dataSource = new BasicDataSource();
    }

    private void setDataSource(String[] args) {
        dataSource.setUrl(args[0]);
        dataSource.setUsername(args[1]);
        dataSource.setPassword(args[2]);
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    private void showAllProducts() throws SQLException {
        Connection connection = dataSource.getConnection();
        productDAO.setConnection(connection);
        List<Product> productList = productDAO.readAll();
        for (Product product : productList) {
            product.print();
        }
    }

    private void showOneProduct() throws SQLException {
        Connection connection = dataSource.getConnection();
        productDAO.setConnection(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a product id: ");
        long id = scanner.nextLong();
        Product product = productDAO.read(id);
        product.print();
    }

    private void addProduct() {
        System.out.println("Enter the information of a new product");
        System.out.print("Enter a product id: ");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter a product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a product price: ");
        Double price = scanner.nextDouble();
        Product product = new Product(id, name, price);
        productDAO.add(product);
    }

    private void updateProduct() {
        System.out.println("Enter the information of a product to be updated");
        System.out.print("Enter a product id: ");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter a product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter a product price: ");
        Double price = scanner.nextDouble();
        Product product = new Product(id, name, price);
        productDAO.update(product);
    }

    private void deleteProduct() {
        System.out.print("Enter a product id to be deleted: ");
        Scanner scanner = new Scanner(System.in);
        long id = scanner.nextLong();
        productDAO.delete(id);
    }

    private void createTableProduct() throws SQLException {
        Connection connection = dataSource.getConnection();
        productDAO.setConnection(connection);
        productDAO.createProductTable();
    }

    public void showMenu() {
        System.out.println("Product management system.");
        System.out.println("1. Read all products");
        System.out.println("2. Read detail of a product by id");
        System.out.println("3. Add a new product");
        System.out.println("4. Update a product");
        System.out.println("5. Delete a product by id");
        System.out.println("6. Exit");
        System.out.print("Please enter your choice: ");
    }

    public static void main(String[] args) throws SQLException {
        Program program = new Program();
        program.setDataSource(args);
        program.createTableProduct();

        int choice = 0;
        while (choice != 6) {
            program.showMenu();
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    program.showAllProducts();
                    break;
                case 2:
                    program.showOneProduct();
                    break;
                case 3:
                    program.addProduct();
                    break;
                case 4:
                    program.updateProduct();
                    break;
                case 5:
                    program.deleteProduct();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Please enter a valid choice");
                    break;
            }
        }
    }
}
