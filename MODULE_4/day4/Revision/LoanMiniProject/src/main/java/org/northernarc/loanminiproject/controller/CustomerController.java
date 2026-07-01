package org.northernarc.loanminiproject.controller;

import org.northernarc.loanminiproject.model.Customer;
import org.northernarc.loanminiproject.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.northernarc.loanminiproject.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Customer>> getCustomersWithOverdueEmis() {
        return ResponseEntity.ok(customerService.findCustomersWithOverdueEmis());
    }

    @GetMapping("/top-defaulters")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getTopDefaulters() {
        return ResponseEntity.ok(customerService.findTopDefaulters());
    }
}

