package org.northernarc.week5_assess.controller;

import org.northernarc.week5_assess.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Object request) {
        return ResponseEntity.status(201).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Object request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}

