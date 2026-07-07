package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerDao extends JpaRepository<Customer,Long> {
	List<Customer> findByNameContainingIgnoreCase(String name);
}
