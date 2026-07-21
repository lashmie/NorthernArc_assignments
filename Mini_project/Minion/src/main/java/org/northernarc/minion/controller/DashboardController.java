package org.northernarc.minion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.northernarc.minion.dto.DashboardDTO;
import org.northernarc.minion.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/dashboard", "/api/dashboard"})
@Tag(name = "Dashboard", description = "Dashboard and reporting endpoints")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final LoanService loanService;

    public DashboardController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "Get dashboard", description = "Retrieve comprehensive dashboard data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Manager role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public DashboardDTO dashboard() {
        logger.info("Fetching dashboard data for Admin/Manager");
        return loanService.getFinalDashboard();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin dashboard", description = "Retrieve admin-only dashboard data (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin dashboard data retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required")
    })
    @SecurityRequirement(name = "Bearer JWT")
    public DashboardDTO adminDashboard() {
        logger.info("Fetching admin-only dashboard data");
        return loanService.getFinalDashboard();
    }
}

