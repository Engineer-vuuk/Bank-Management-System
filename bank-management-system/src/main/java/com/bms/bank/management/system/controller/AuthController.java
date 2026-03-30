package com.bms.bank.management.system.controller;

import com.bms.bank.management.system.dto.AuthRequest;
import com.bms.bank.management.system.dto.AuthResponse;
import com.bms.bank.management.system.model.Role;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.repository.UserRepository;
import com.bms.bank.management.system.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtService jwtService,
                          UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        long accountNumber = 1000000000000L + (long) (random.nextDouble() * 9000000000000L);
        return String.valueOf(accountNumber);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Username already taken", null, null, null, null));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Email already in use", null, null, null, null));
        }

        if (!request.getPin().matches("\\d{4}")) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse("Transaction PIN must be exactly 4 digits", null, null, null, null));
        }

        String accountNumber = generateAccountNumber();
        String hashedPin = passwordEncoder.encode(request.getPin());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setDob(request.getDob());
        user.setKinName(request.getKinName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setKinPhoneNumber(request.getKinPhoneNumber());
        user.setIdNumber(request.getIdNumber());
        user.setPin(hashedPin);
        user.setAccountNumber(accountNumber);

        // Assign role dynamically
        if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
            user.setRoles(Set.of(Role.ADMIN));
        } else {
            user.setRoles(Set.of(Role.USER));
        }

        userRepository.save(user);

        return ResponseEntity.ok(new AuthResponse("User registered successfully! Please wait for atleast two minutes as the admins approve your account", null, null, request.getRole() != null ? request.getRole().toUpperCase() : "USER", accountNumber));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        System.out.println("🔍 Received Login Request: Username = " + request.getUsername());

        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            System.out.println("❌ User not found.");
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or PIN", null, null, null, null));
        }

        User user = userOptional.get();
        System.out.println("✅ Found user: " + user.getUsername());

        if (!passwordEncoder.matches(request.getPin(), user.getPin())) {
            System.out.println("❌ PIN mismatch.");
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or PIN", null, null, null, null));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails, request.getPin());

        return ResponseEntity.ok(new AuthResponse("Login successful", token, user.getUsername(), user.getRoles().contains(Role.ADMIN) ? "ADMIN" : "USER", user.getAccountNumber()));
    }
}
