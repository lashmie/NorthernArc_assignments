package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDao extends JpaRepository<Order,Long> {
	List<Order> findByCustomerId(Long customerId);
	List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
}
