package org.northernarc.ecom.controller;

import jakarta.validation.Valid;
import org.northernarc.ecom.DTO.CustomerRequestDTO;
import org.northernarc.ecom.DTO.CustomerResponseDTO;
import org.northernarc.ecom.DTO.CustomerUpdateDTO;
import org.northernarc.ecom.model.Order;
import org.northernarc.ecom.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ecom/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> findAll() {

        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> findById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(
            @Valid @RequestBody CustomerRequestDTO customerDTO) {

        return ResponseEntity
                .status(201)
                .body(customerService.saveCustomer(customerDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody CustomerUpdateDTO customerDTO) {

        return ResponseEntity.ok(
                customerService.updateCustomer(id, customerDTO)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/orders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getCustomerOrders(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                customerService.getCustomerOrders(id)
        );
    }
}