package org.northernarc.minion.service;

import org.northernarc.minion.dto.AuthResponse;
import org.northernarc.minion.dto.LoginRequest;
import org.northernarc.minion.dto.RegisterRequest;
import org.northernarc.minion.exception.UnauthorizedException;
import org.northernarc.minion.model.Customer;
import org.northernarc.minion.model.Role;
import org.northernarc.minion.repository.CustomerRepository;
import org.northernarc.minion.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(CustomerRepository customerRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Customer register(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setCustomerName(request.customerName());
        customer.setEmail(request.email());
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setPhoneNumber(request.phoneNumber());
        customer.setCity(request.city());
        customer.setCreditScore(700);
        customer.setRole(Role.USER);
        return customerRepository.save(customer);
    }

    public AuthResponse login(LoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.email());
        if (customer == null) {
            throw new UnauthorizedException("Invalid email or password");
        }

        boolean valid = passwordEncoder.matches(request.password(), customer.getPassword());
        if (!valid) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtService.generateToken(customer);
        return new AuthResponse(token, jwtService.extractExpiry(token), customer.getRole().name());
    }
}

