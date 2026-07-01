package org.northernarc.ecom.controller;

import jakarta.validation.Valid;
import org.northernarc.ecom.DTO.OrderRequestDTO;
import org.northernarc.ecom.DTO.OrderResponseDTO;
import org.northernarc.ecom.DTO.OrderUpdateDTO;
import org.northernarc.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>> getAll() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> getById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> save(
            @Valid @RequestBody OrderRequestDTO dto) {

        return ResponseEntity.status(201)
                .body(orderService.placeOrder(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody OrderUpdateDTO dto) {

        return ResponseEntity.ok(
                orderService.updateOrder(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id) {

        orderService.cancelOrder(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDTO>>
    getOrdersByCustomer(@PathVariable Integer id) {

        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(id)
        );
    }
}