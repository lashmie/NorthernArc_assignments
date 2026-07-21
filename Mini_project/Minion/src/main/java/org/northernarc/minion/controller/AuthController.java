package org.northernarc.minion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.northernarc.minion.dto.AuthResponse;
import org.northernarc.minion.dto.LoginRequest;
import org.northernarc.minion.dto.RegisterRequest;
import org.northernarc.minion.model.Customer;
import org.northernarc.minion.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

   // private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register new customer", description = "Register a new customer in the system (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required"),
            @ApiResponse(responseCode = "400", description = "Invalid registration request")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public ResponseEntity<Customer> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Processing customer registration for email: {}", request.email());
        Customer customer = authService.register(request);
        logger.info("Customer registered successfully with ID: {}", customer.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and receive JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid login request")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Processing login request for email: {}", request.email());
        AuthResponse response = authService.login(request);
        logger.info("Login successful for email: {}", request.email());
        return ResponseEntity.ok(response);
    }
}

