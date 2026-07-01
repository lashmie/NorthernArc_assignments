package org.northernarc.loanminiproject.controller;

import org.northernarc.loanminiproject.model.EmiSchedule;
import org.northernarc.loanminiproject.service.EmiScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emis")
public class EmiScheduleController {

    @Autowired
    private EmiScheduleService emiScheduleService;

    @GetMapping("/highest-overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<EmiSchedule>> getHighestOverdueAmount() {
        return ResponseEntity.ok(emiScheduleService.findHighestOverdueAmount());
    }
}

