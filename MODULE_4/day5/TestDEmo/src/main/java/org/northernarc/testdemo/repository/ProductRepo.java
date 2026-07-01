package org.northernarc.testdemo.repository;

import org.northernarc.testdemo.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepo {
    private final Map<String, Product> productStore = new ConcurrentHashMap<>();

    public Product saveProduct(Product product) {
        productStore.put(product.getId(), product);
        return product;
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(productStore.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    public Optional<Product> updateProduct(String id, Product product) {
        if (!productStore.containsKey(id)) {
            return Optional.empty();
        }
        product.setId(id);
        productStore.put(id, product);
        return Optional.of(product);
    }

    public boolean deleteProduct(String id) {
        return productStore.remove(id) != null;
    }
}
