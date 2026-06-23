package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Product;

import java.util.List;

public class ProductServiceImpl implements ProductService{
    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product getProduct(Long id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return List.of();
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return List.of();
    }

    @Override
    public void deleteProduct(Long id) {

    }
}
