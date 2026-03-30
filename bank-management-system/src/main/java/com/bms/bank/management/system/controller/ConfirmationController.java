package com.bms.bank.management.system.controller;

import com.bms.bank.management.system.service.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmationController {

    @Autowired
    private ConfirmationService confirmationService;

    // Endpoint to confirm account using token
    @GetMapping("/api/auth/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        try {
            confirmationService.confirmAccount(token);
            return ResponseEntity.ok("Account confirmed successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
