package com.bms.bank.management.system.controller;

import com.bms.bank.management.system.dto.DepositRequest;
import com.bms.bank.management.system.dto.WithdrawRequest;
import com.bms.bank.management.system.dto.TransferRequest;  // ✅ Import TransferRequest DTO
import com.bms.bank.management.system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@RequestBody DepositRequest request) {
        try {
            Map<String, Object> response = transactionService.deposit(request);
            return ResponseEntity.ok(response);  // ✅ Return structured JSON response
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestBody WithdrawRequest request) {
        try {
            Map<String, Object> response = transactionService.withdraw(request);
            return ResponseEntity.ok(response);  // ✅ Return structured JSON response
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ New Transfer Endpoint
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody TransferRequest request) {
        try {
            Map<String, Object> response = transactionService.transfer(request);
            return ResponseEntity.ok(response);  // ✅ Return structured JSON response
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}