package com.bms.bank.management.system.config;

import com.bms.bank.management.system.model.User;
import com.bms.bank.management.system.service.JwtService;
import com.bms.bank.management.system.service.SecurityHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final SecurityHelper securityHelper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationSuccessHandler(JwtService jwtService, SecurityHelper securityHelper) {
        this.jwtService = jwtService;
        this.securityHelper = securityHelper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Retrieve user details including PIN from SecurityHelper
        User user = securityHelper.findByUsername(userDetails.getUsername());
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return;
        }

        String token = jwtService.generateToken(userDetails, user.getPin()); // Pass PIN for token generation

        // Prepare response data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "Authentication successful");
        responseData.put("token", token);
        responseData.put("username", userDetails.getUsername());
        responseData.put("roles", userDetails.getAuthorities());
        responseData.put("accountNumber", user.getAccountNumber());

        // Set response properties
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}