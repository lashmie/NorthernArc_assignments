package org.northernarc.productrental.controller;

import jakarta.validation.Valid;
import org.northernarc.productrental.dto.LoginRequestDTO;
import org.northernarc.productrental.dto.LoginResponseDTO;
import org.northernarc.productrental.repository.CustomerRepository;
import org.northernarc.productrental.security.JwtUtil;
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

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    CustomerRepository customerRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtUtil.generateTokenFromAuthentication(authentication);

            String role = "USER";

            return ResponseEntity.ok(LoginResponseDTO.builder()
                    .token(token)
                    .email(request.getEmail())
                    .role(role)
                    .build());
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody LoginRequestDTO request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Registration endpoint available without UserRepository",
                        "encodedSample", passwordEncoder.encode(request.getPassword())
                ));
    }
}

