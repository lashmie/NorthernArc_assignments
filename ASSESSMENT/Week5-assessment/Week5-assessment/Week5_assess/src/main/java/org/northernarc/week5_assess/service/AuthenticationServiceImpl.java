package org.northernarc.week5_assess.service;

import java.util.Map;

import org.northernarc.week5_assess.entity.Customer;
import org.northernarc.week5_assess.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    public AuthenticationServiceImpl(
        CustomerRepository customerRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager
    ) {
    }

    public AuthResponse register(RegisterRequest request) {
        return null;
    }

    public AuthResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public Object register(Object request) {
        return null;
    }

    @Override
    public Object login(Object request) {
        return null;
    }

    private Customer buildCustomer(RegisterRequest request) {
        return null;
    }

    private AuthResponse tokenResponse(String token) {
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return response;
    }

    private Map<String, Object> customerMap(Customer customer) {
        return Map.of();
    }
}

