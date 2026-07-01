package org.northernarc.ecom.controller;

import jakarta.validation.Valid;
import org.northernarc.ecom.DTO.OrderItemRequestDTO;
import org.northernarc.ecom.DTO.OrderItemResponseDTO;
import org.northernarc.ecom.DTO.OrderItemUpdateDTO;
import org.northernarc.ecom.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/orderitem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderItemResponseDTO>> findAll() {

        return ResponseEntity.ok(
                orderItemService.getAllOrderItems()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                orderItemService.getOrderItemById(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> save(
            @Valid @RequestBody OrderItemRequestDTO dto) {

        return ResponseEntity.status(201)
                .body(orderItemService.saveOrderItem(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderItemResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody OrderItemUpdateDTO dto) {

        return ResponseEntity.ok(
                orderItemService.updateOrderItem(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id) {

        orderItemService.deleteOrderItem(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderItemResponseDTO>>
    getItemsByOrder(@PathVariable Integer id) {

        return ResponseEntity.ok(
                orderItemService.getItemsByOrder(id)
        );
    }
}