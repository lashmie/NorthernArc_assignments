package org.northernarc.ecom.repository;

import org.northernarc.ecom.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {
    public List<OrderItem> findByOrderId(int orderid);
}
