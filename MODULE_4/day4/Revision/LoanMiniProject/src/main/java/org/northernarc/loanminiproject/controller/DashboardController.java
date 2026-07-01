package org.northernarc.loanminiproject.controller;

import org.northernarc.loanminiproject.dto.DashboardDto;
import org.northernarc.loanminiproject.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<DashboardDto> getDashboard() {
        DashboardDto dashboard = dashboardService.getDashboardMetrics();
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }
}

