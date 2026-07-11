package com.oem.oem_portal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oem.oem_portal.dtos.request.BankerRegisterRequest;
import com.oem.oem_portal.dtos.request.LoginRequest;
import com.oem.oem_portal.dtos.request.VendorRegisterRequest;
import com.oem.oem_portal.dtos.response.ApiResponse;
import com.oem.oem_portal.dtos.response.AuthResponse;
import com.oem.oem_portal.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
            ApiResponse.success("Login sucessful", response)
        );
    }

    @PostMapping("/register/banker")
    public ResponseEntity<ApiResponse<AuthResponse>> registerBanker(@Valid @RequestBody BankerRegisterRequest request) {
        AuthResponse response = authService.registerBanker(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(
                "Banker registered sucessfully", response
            ));
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<ApiResponse<AuthResponse>> registerVendor(@Valid @RequestBody VendorRegisterRequest request) {

        AuthResponse response = authService.registerVendor(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Vendor registred sucessfully", response
            ));
    }

}
