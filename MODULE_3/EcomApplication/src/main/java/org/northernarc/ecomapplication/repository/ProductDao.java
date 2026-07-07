package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Long> {
	List<Product> findByNameContainingIgnoreCase(String name);
	List<Product> findByCategoryContainingIgnoreCase(String category);
	List<Product> findByBrandContainingIgnoreCase(String brand);
}
