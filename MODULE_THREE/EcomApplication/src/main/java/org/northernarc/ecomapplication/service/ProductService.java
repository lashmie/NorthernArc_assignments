package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Product;

import java.util.List;

public interface ProductService {
Product saveProduct(Product product);
Product getProduct(Long id);
List<Product> getAllProducts();
List<Product> searchProducts(String name, String category, String brand);
List<Product> getProductsByName(String name);
List<Product> getProductsByCategory(String category);
List<Product> getProductsByBrand(String brand);
void deleteProduct(Long id);
}
