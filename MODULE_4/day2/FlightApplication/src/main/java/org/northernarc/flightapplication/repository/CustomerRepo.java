package org.northernarc.flightapplication.repository;

import org.northernarc.flightapplication.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
}
