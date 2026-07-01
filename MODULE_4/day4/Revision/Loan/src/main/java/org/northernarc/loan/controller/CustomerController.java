package org.northernarc.loan.controller;

import jakarta.validation.Valid;
import org.northernarc.loan.exception.NotFoundException;
import org.northernarc.loan.model.Customer;
import org.northernarc.loan.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) {
        if (customer.getRole() == null || customer.getRole().isBlank()) {
            customer.setRole("USER");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"))
        );
    }
}

