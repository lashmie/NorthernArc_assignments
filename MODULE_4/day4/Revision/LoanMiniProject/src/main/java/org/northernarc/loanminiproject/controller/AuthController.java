package org.northernarc.loanminiproject.controller;

import org.northernarc.loanminiproject.dto.LoginRequestDto;
import org.northernarc.loanminiproject.dto.LoginResponseDto;
import org.northernarc.loanminiproject.model.Customer;
import org.northernarc.loanminiproject.model.Role;
import org.northernarc.loanminiproject.repository.CustomerRepository;
import org.northernarc.loanminiproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtUtil.generateToken(loginRequest.getEmail());

            Customer customer = customerRepository.findByEmailOptional(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            LoginResponseDto response = new LoginResponseDto();
            response.setToken(token);
            response.setEmail(customer.getEmail());
            response.setRole(customer.getRole().toString());
            response.setMessage("Login successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            LoginResponseDto errorResponse = new LoginResponseDto();
            errorResponse.setMessage("Invalid email or password");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody Customer customer) {
        try {
            if (customerRepository.findByEmailOptional(customer.getEmail()).isPresent()) {
                LoginResponseDto errorResponse = new LoginResponseDto();
                errorResponse.setMessage("Email already registered");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }

            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setRole(Role.USER);

            Customer savedCustomer = customerRepository.save(customer);

            String token = jwtUtil.generateToken(savedCustomer.getEmail());

            LoginResponseDto response = new LoginResponseDto();
            response.setToken(token);
            response.setEmail(savedCustomer.getEmail());
            response.setRole(savedCustomer.getRole().toString());
            response.setMessage("Registration successful");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            LoginResponseDto errorResponse = new LoginResponseDto();
            errorResponse.setMessage("Registration failed: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

