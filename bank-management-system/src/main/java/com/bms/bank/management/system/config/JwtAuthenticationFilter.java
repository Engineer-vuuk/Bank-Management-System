package com.bms.bank.management.system.config;

import com.bms.bank.management.system.service.JwtService;
import com.bms.bank.management.system.service.SecurityHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component  // This registers JwtAuthenticationFilter as a Spring Bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    private final JwtService jwtService;
    private final SecurityHelper securityHelper;

    public JwtAuthenticationFilter(JwtService jwtService, SecurityHelper securityHelper) {
        this.jwtService = jwtService;
        this.securityHelper = securityHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        logger.info("Checking Authorization header...");

        // Check if the Authorization header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warning("No valid Authorization header found, skipping filter...");
            chain.doFilter(request, response);
            return;
        }

        // Extract the token
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username == null) {
            logger.warning("Failed to extract username from token, skipping authentication...");
            chain.doFilter(request, response);
            return;
        }

        logger.info("Extracted username from token: " + username);

        // Check if the user is already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = securityHelper.loadUserByUsername(username);
            logger.info("Loaded user details for: " + userDetails.getUsername());

            // Validate the token and authenticate the user
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Authentication successful for user: " + username);
            } else {
                logger.warning("Token validation failed for user: " + username);
            }
        }

        // Proceed with the next filter in the chain
        chain.doFilter(request, response);
    }
}
