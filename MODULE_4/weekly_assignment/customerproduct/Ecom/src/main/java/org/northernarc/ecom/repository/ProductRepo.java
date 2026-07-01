package org.northernarc.ecom.repository;

import org.northernarc.ecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.brand = :brand")
    List<Product> findProductByBrand(@Param("brand") String brand);

    public List<Product> findProductByCategory(String category);

    public List<Product> findProductByName(String name);
}
