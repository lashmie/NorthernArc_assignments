package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<Product, Long> {

}
