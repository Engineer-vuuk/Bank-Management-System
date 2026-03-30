package com.bms.bank.management.system.service;

import com.bms.bank.management.system.model.Role;
import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.logging.Logger;

@Service
public class AuthenticationService {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());
    private final SecurityHelper securityHelper;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthenticationService(
            SecurityHelper securityHelper,
            UserDetailsService userDetailsService,
            UserRepository userRepository) {
        this.securityHelper = securityHelper;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    // Authenticate using only the transaction pin
    public String authenticateUser(String email, String transactionPin) {
        // Fetch the user from the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Check if the provided transaction pin matches
        if (!user.getPin().equals(transactionPin)) {
            throw new RuntimeException("Invalid transaction PIN.");
        }

        // Fetch user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Log the roles of the authenticated user
        if (userDetails instanceof User) {
            LOGGER.info("User authenticated with roles: " + ((User) userDetails).getRoles());
        }

        return securityHelper.generateToken(userDetails.getUsername());
    }

    // Register user with only the transaction pin
    public User registerUser(String username, String email, String transactionPin, Role role, User adminUser) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists!");
        }

        if (role != Role.USER && (adminUser == null || !adminUser.getRoles().contains(Role.ADMIN))) {
            throw new RuntimeException("Only admins can assign roles other than USER!");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRoles(Set.of(role));
        user.setPin(transactionPin);  // Set the transaction pin during registration

        User savedUser = userRepository.save(user);

        // Log assigned role
        LOGGER.info("New user registered with role: " + savedUser.getRoles());

        return savedUser;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDetailsService.loadUserByUsername(email);
    }
}