package daoArchitecture.dao;

import daoArchitecture.entity.Product;
import java.util.List;

public interface ProductDao {

    void save(Product product);

    Product findById(int id);

    void update(Product product);

    void deleteById(int id);

    Iterable<Product> findAll();

    void deleteAll();

    List<Product> findByCategory(String category);

    List<Product> findByBrand(String brand);
}
