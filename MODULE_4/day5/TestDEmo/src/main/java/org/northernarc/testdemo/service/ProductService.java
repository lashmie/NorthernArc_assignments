package org.northernarc.testdemo.service;

import org.northernarc.testdemo.model.Product;
import org.northernarc.testdemo.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product saveProduct(Product product) {
        return productRepo.saveProduct(product);
    }

    public Optional<Product> updateProduct(String id, Product product) {
        return productRepo.updateProduct(id, product);
    }

    public boolean deleteProduct(String id) {
        return productRepo.deleteProduct(id);
    }

    public Optional<Product> getProductById(String id) {
        return productRepo.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
}

