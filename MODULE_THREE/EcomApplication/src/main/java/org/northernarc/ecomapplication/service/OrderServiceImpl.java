package org.northernarc.ecomapplication.service;

import org.northernarc.ecomapplication.model.Customer;
import org.northernarc.ecomapplication.model.Order;
import org.northernarc.ecomapplication.model.Order_Item;
import org.northernarc.ecomapplication.repository.CustomerDao;
import org.northernarc.ecomapplication.repository.OrderDao;
import org.northernarc.ecomapplication.repository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //validate customer
        Customer customer =customerRepository.findById(CustomerId).orElseThrow(()->new RuntimeException("customer not found.."));
        //create order
        Order order = new Order();
        order.setCustomer(customer);
        double total=0;
    }

    @Override
    public Order getOrderById(Long OrderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return List.of();
    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public void cancelOrder(Long orderId) {

    }
}
