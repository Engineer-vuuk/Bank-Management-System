package com.bms.bank.management.system.service;

import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SecurityHelper implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username); // Now finding by username
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // Use username instead of email
                user.getPin(), // Store PIN for authentication
                authorities
        );
    }

    // ✅ Now correctly finds by username instead of email
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User authenticateAndGetUser(String username, String pin) {
        User user = findByUsername(username);

        if (!user.getPin().equals(pin)) {
            throw new RuntimeException("Invalid PIN");
        }

        return user;
    }

    public String generateToken(String username) {
        return username;
    }
}


