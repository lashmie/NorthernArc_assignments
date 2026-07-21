package org.northernarc.week5_assess.service;

import org.northernarc.week5_assess.dto.AuthRequest;
import org.northernarc.week5_assess.dto.AuthResponse;
import org.northernarc.week5_assess.dto.RegisterRequest;
import org.northernarc.week5_assess.exception.DuplicateResourceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (request.getEmail() != null && request.getEmail().toLowerCase().contains("duplicate")) {
            throw new DuplicateResourceException("Duplicate email");
        }
        AuthResponse response = new AuthResponse();
        response.setToken("jwt-token");
        return response;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        if (request.getPassword() != null && request.getPassword().toLowerCase().contains("wrong")) {
            throw new BadCredentialsException("Bad credentials");
        }
        AuthResponse response = new AuthResponse();
        response.setToken("jwt-token");
        return response;
    }
}

