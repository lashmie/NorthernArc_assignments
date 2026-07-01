package org.northernarc.jpauserexplanation.controller;

import org.northernarc.jpauserexplanation.dto.JwtRequestDTO;
import org.northernarc.jpauserexplanation.dto.JwtResponseDTO;
import org.northernarc.jpauserexplanation.util.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/auth/login")
    public JwtResponseDTO login(@RequestBody JwtRequestDTO jwtRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
        );

        String token = jwtUtil.generateToken(jwtRequest.getUsername());
        return new JwtResponseDTO(token);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String user() {
        return "Hello User!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Hello Admin!";
    }
}
