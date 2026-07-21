apackage org.northernarc.week5_assess.controller;

import java.util.List;

import org.northernarc.week5_assess.dto.TransactionHistoryResponse;
import org.northernarc.week5_assess.dto.TransferRequest;
import org.northernarc.week5_assess.dto.TransferResponse;
import org.northernarc.week5_assess.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok((TransferResponse) transactionService.transfer(request));
    }

    @GetMapping("/{accountNumber}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<TransactionHistoryResponse>> history(@PathVariable String accountNumber) {
        return ResponseEntity.ok((List<TransactionHistoryResponse>) transactionService.getTransactionsForAccount(accountNumber));
    }
}

