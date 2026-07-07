package daoArchitecture.ui;
import daoArchitecture.dao.ProductDao;
import daoArchitecture.dao.ProductDaoImpl;
import daoArchitecture.entity.Product;

import java.util.List;

public class ProductApp {

    public static void main(String[] args) {

        ProductDao dao = new ProductDaoImpl();

        dao.save(new Product(
                101,
                "iPhone 15",
                "Apple",
                "Mobile",
                79999,
                10));

        dao.save(new Product(
                102,
                "Galaxy S24",
                "Samsung",
                "Mobile",
                69999,
                15));

        dao.save(new Product(
                103,


                "Inspiron",
                "Dell",
                "Laptop",
                55000,
                8));

        System.out.println("All Products");
        List<Product> products = dao.findAll();

        for(Product product : products) {
            System.out.println(product);
        }

        System.out.println("\nFind By Id");
        List<Product> mobiles = dao.findByCategory("Mobile");

        for(Product product : mobiles) {
            System.out.println(product);
        }
        System.out.println("\nFind By Id");

        Product product1 = dao.findById(101);

        if(product1 != null) {
            System.out.println(product1);
        } else {
            System.out.println("Product not found");
        }

        System.out.println("\nFind By Category");
        List<Product> mobile = dao.findByCategory("Mobile");
        for(Product product : mobile) {
            System.out.println(product);
        }

        System.out.println("\nFind By Brand");
        List<Product> appleProducts = dao.findByBrand("Apple");
        for(Product product : appleProducts) {
            System.out.println(product);
        }
    }
}
