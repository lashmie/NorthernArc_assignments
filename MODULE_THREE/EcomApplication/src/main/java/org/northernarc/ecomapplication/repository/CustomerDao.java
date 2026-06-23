package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer,Long> {
}
