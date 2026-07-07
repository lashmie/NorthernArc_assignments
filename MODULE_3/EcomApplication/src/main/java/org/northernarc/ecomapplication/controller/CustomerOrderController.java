package org.northernarc.ecomapplication.controller;

import jakarta.validation.Valid;
import org.northernarc.ecomapplication.dto.OrderItemRequest;
import org.northernarc.ecomapplication.dto.PlaceOrderRequest;
import org.northernarc.ecomapplication.model.Order;
import org.northernarc.ecomapplication.model.Order_Item;
import org.northernarc.ecomapplication.model.Product;
import org.northernarc.ecomapplication.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Order placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return orderService.PlaceOrder(request.getCustomerId(), mapOrderItems(request.getItems()));
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

