package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Customer;
import org.northernarc.ecomapplication.model.Order;
import org.northernarc.ecomapplication.model.Order_Item;
import org.northernarc.ecomapplication.model.Product;
import org.northernarc.ecomapplication.repository.CustomerDao;
import org.northernarc.ecomapplication.repository.OrderDao;
import org.northernarc.ecomapplication.repository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderRepository;
    @Autowired
    private CustomerDao customerRepository;
    @Autowired
    private ProductDao productRepository;

    @Override
    public Order PlaceOrder(Long CustomerId, List<Order_Item> items) {
        Customer customer = customerRepository.findById(CustomerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + CustomerId));

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("At least one order item is required.");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(Date.valueOf(LocalDate.now()));
        order.setDelvieryDate(Date.valueOf(LocalDate.now().plusDays(5)));

        List<Order_Item> preparedItems = new ArrayList<>();
        for (Order_Item item : items) {
            if (item.getProduct() == null || item.getProduct().getId() == null) {
                throw new RuntimeException("Each item must include product id.");
            }

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProduct().getId()));

            if (item.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than zero.");
            }

            Order_Item orderItem = new Order_Item();
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            preparedItems.add(orderItem);
        }

        order.setOrderItems(preparedItems);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long OrderId) {
        return orderRepository.findById(OrderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + OrderId));
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> getOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void cancelOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
