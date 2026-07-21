package org.northernarc.minion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.northernarc.minion.model.Customer;
import org.northernarc.minion.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Customer Management", description = "Customer creation and reporting endpoints")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    //private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Create new customer", description = "Create a new customer record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required"),
            @ApiResponse(responseCode = "400", description = "Invalid customer request")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        logger.info("Creating new customer with email: {}", customer.getEmail());
        Customer createdCustomer = customerService.createCustomer(customer);
        logger.info("Customer created successfully with ID: {}", createdCustomer.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @GetMapping("/reports/overdue")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get customers with overdue EMIs", description = "Retrieve list of customers who have overdue EMI payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public List<Customer> customersWithOverdueEmis() {
        logger.info("Fetching customers with overdue EMIs");
        return customerService.getCustomersWithOverdueEmis();
    }

    @GetMapping("/reports/top-defaulters")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get top defaulters", description = "Retrieve list of top loan defaulters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top defaulters retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public List<Customer> topDefaulters() {
        logger.info("Fetching top defaulters report");
        return customerService.getTopDefaulters();
    }
}

