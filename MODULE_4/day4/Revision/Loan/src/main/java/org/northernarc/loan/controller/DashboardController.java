package org.northernarc.loan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping
    public ResponseEntity<Map<String, String>> dashboard() {
        return ResponseEntity.ok(Map.of("message", "dashboard"));
    }

    @GetMapping("/admin")
    public ResponseEntity<Map<String, String>> adminDashboard() {
        return ResponseEntity.ok(Map.of("message", "admin-dashboard"));
    }
}

