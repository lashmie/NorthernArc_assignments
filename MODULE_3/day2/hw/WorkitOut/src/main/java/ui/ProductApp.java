package ui;

import dao.ProductDao;
import dao.ProductDaoImpl;
import entity.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductApp {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        ProductDao productDao = new ProductDaoImpl();
        while(true){
            System.out.println("\n===== PRODUCT MENU =====");
            System.out.println("1 -> Save Product");
            System.out.println("2 -> Update Product");
            System.out.println("3 -> Delete by ID");
            System.out.println("4 -> Delete by Name");
            System.out.println("5 -> Delete All");
            System.out.println("6 -> Find by ID");
            System.out.println("7 -> Find by Name");
            System.out.println("8 -> Find All");
            System.out.println("0 -> Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice){

                case 1 -> {
                    System.out.println("Enter the product id: ");
                    int id = sc.nextInt();

                    System.out.println("Enter the price of the product: ");
                    double price = sc.nextDouble();

                    sc.nextLine(); // 🔥 consume leftover newline

                    System.out.println("Enter the product Name: ");
                    String name = sc.nextLine();

                    productDao.save(new Product(id, name, price));
                }
                case 2->{
                    System.out.println("Enter the product id: ");
                    int id= sc.nextInt();
                    System.out.println("Enter the price of the product: ");
                    Double price =sc.nextDouble();
                    System.out.println("Enter the product Name: ");
                    String name =sc.next();
                    productDao.update(new Product(id,name,price));
                }
                case 3->{
                    System.out.println("Enter the id to delete");
                    int id=sc.nextInt();
                    productDao.deletebyid(id);
                }
                case 4 -> {
                    System.out.print("Enter name: ");
                    String name = sc.next();
                    productDao.deletebyname(name);
                }

                case 5 -> {
                    productDao.deleteAll();
                }
                case 6->{
                    System.out.println("Enter the id : ");
                    int id= sc.nextInt();
                    Product p =productDao.findbyid(id);
                    System.out.println(p);
                }
                case 7->{
                    System.out.print("Enter name: ");
                    String name = sc.next();

                    Product p = productDao.findbyname(name);
                    System.out.println(p);
                }
                    case 8 -> {
                        List<Product> list = productDao.findall();
                        for (Product p : list) {
                            System.out.println(p);
                        }
                }
                case 0->{
                    System.out.println("Exiting.......");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid choice");
                }
            }
        }


    }
}
