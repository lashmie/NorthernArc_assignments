package org.northernarc.loanminiproject.controller;

import org.northernarc.loanminiproject.dto.CustomerPaymentSummaryDto;
import org.northernarc.loanminiproject.dto.PaymentSummaryDto;
import org.northernarc.loanminiproject.model.EmiPayment;
import org.northernarc.loanminiproject.service.EmiPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class EmiPaymentController {

    @Autowired
    private EmiPaymentService emiPaymentService;

    @GetMapping("/latest")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('USER')")
    public ResponseEntity<EmiPayment> getLatestPayment() {
        EmiPayment latestPayment = emiPaymentService.findLatestPayment();
        if (latestPayment != null) {
            return ResponseEntity.ok(latestPayment);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/collection-by-city")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<PaymentSummaryDto>> getTotalCollectionByCity() {
        return ResponseEntity.ok(emiPaymentService.getTotalEmiCollectionByCity());
    }

    @GetMapping("/latest-by-customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<CustomerPaymentSummaryDto>> getLatestPaymentByCustomer() {
        return ResponseEntity.ok(emiPaymentService.getLatestPaymentByCustomer());
    }
}

