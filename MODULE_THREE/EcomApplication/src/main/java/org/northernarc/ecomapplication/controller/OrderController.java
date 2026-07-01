package org.northernarc.ecomapplication.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.northernarc.ecomapplication.dto.OrderItemRequest;
import org.northernarc.ecomapplication.dto.PlaceOrderRequest;
import org.northernarc.ecomapplication.model.Order;
import org.northernarc.ecomapplication.model.Order_Item;
import org.northernarc.ecomapplication.model.Product;
import org.northernarc.ecomapplication.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return orderService.PlaceOrder(request.getCustomerId(), mapOrderItems(request.getItems()));
    }

    @PostMapping("/customer/{customerId}/place")
    public Order placeOrderByCustomer(
            @PathVariable Long customerId,
            @NotEmpty @Valid @RequestBody List<OrderItemRequest> items) {
        return orderService.PlaceOrder(customerId, mapOrderItems(items));
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomerId(customerId);
    }

    @GetMapping("/search/by-customer-name")
    public List<Order> getOrdersByCustomerName(@RequestParam String customerName) {
        return orderService.getOrdersByCustomerName(customerName);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    private List<Order_Item> mapOrderItems(List<OrderItemRequest> itemRequests) {
        List<Order_Item> items = new ArrayList<>();

        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = new Product();
            product.setId(itemRequest.getProductId());

            Order_Item item = new Order_Item();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            items.add(item);
        }

        return items;
    }
}

