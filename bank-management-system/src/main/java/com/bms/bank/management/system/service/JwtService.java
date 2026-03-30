package com.bms.bank.management.system.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final Logger logger = Logger.getLogger(JwtService.class.getName());

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Generate JWT Token
     */
    public String generateToken(UserDetails userDetails, String pin) {
        try {
            JWSSigner signer = new MACSigner(secretKey.getBytes());

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername()) // Username is used for querying
                    .claim("roles", roles)
                    .claim("pin", pin)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            logger.severe("Error generating JWT token: " + e.getMessage());
            throw new RuntimeException("Error generating JWT token", e);
        }
    }

    /**
     * Extract username from JWT token
     */
    public String extractUsername(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                logger.severe("Token is null or empty");
                throw new RuntimeException("Invalid token: Token is null or empty");
            }

            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject(); // Username stored in subject
        } catch (Exception e) {
            logger.severe("Error extracting username from token: " + e.getMessage());
            throw new RuntimeException("Error extracting username from token", e);
        }
    }
    /**
     * Extract roles from JWT token
     */
    public List<String> extractRoles(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringListClaim("roles");
        } catch (Exception e) {
            logger.severe("Error extracting roles from token: " + e.getMessage());
            throw new RuntimeException("Error extracting roles from token", e);
        }
    }

    /**
     * Validate JWT token using username
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = userDetails.getUsername(); // Get username from UserDetails
            String extractedUsername = extractUsername(token); // Extract username from token

            boolean isValid = extractedUsername.equals(username) && !isTokenExpired(token);

            logger.info("Token validation for " + username + ": " + (isValid ? "Valid" : "Invalid"));
            return isValid;
        } catch (Exception e) {
            logger.severe("Error validating token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if the token has expired
     */
    private boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isExpired = new Date().after(signedJWT.getJWTClaimsSet().getExpirationTime());
            logger.info("Token expiration check: " + (isExpired ? "Expired" : "Valid"));
            return isExpired;
        } catch (Exception e) {
            logger.severe("Error checking token expiration: " + e.getMessage());
            throw new RuntimeException("Error checking token expiration", e);
        }
    }

    /**
     * Extract PIN from JWT token
     */
    public String extractPin(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getStringClaim("pin");
        } catch (Exception e) {
            logger.severe("Error extracting PIN from token: " + e.getMessage());
            throw new RuntimeException("Error extracting PIN from token", e);
        }
    }
}