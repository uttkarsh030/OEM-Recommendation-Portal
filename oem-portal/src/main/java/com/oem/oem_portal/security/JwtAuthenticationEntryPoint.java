package com.oem.oem_portal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oem.oem_portal.dtos.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        // Step 1: Set response status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Step 2: Tell frontend we are sending JSON
        response.setContentType("application/json");

        // Step 3: Build our error response
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(false)
                .message("Unauthorized: Please login to access this resource")
                .data(null)
                .build();

        // Step 4: Convert Java object to JSON string
        ObjectMapper mapper = new ObjectMapper();

        // Step 5: Write JSON to response
        response.getWriter().write(
                mapper.writeValueAsString(apiResponse)
        );
    }

}