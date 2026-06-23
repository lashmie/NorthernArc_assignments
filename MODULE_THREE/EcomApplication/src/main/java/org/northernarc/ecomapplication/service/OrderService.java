package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Order;
import org.northernarc.ecomapplication.model.Order_Item;

import java.util.List;

public interface OrderService {
Order PlaceOrder(Long CustomerId, List<Order_Item> items);
Order getOrderById(Long OrderId);
List<Order> getOrdersByCustomerId(Long customerId);
List<Order> getAllOrders();
void cancelOrder(Long orderId);
}
