package org.northernarc.ecomapplication.repository;

import org.northernarc.ecomapplication.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order,Long> {
}
