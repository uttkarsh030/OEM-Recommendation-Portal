package com.oem.oem_portal.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oem.oem_portal.dtos.request.RecommendationRequest;
import com.oem.oem_portal.dtos.response.ApiResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.dtos.response.VendorDashboardResponse;
import com.oem.oem_portal.service.VendorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VENDOR')")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<VendorDashboardResponse>> getDashboard(Authentication authentication) {
        String email = authentication.getName();
        VendorDashboardResponse dashboardResponse = vendorService.getDashboard(email);

        return ResponseEntity.ok(
            ApiResponse.success("Dashboard loaded", dashboardResponse)
        );
    }

    @PostMapping("/recommendations")
    public ResponseEntity<ApiResponse<RecommendationResponse>> addRecommendation(Authentication authentication, @Valid @RequestBody RecommendationRequest request) {
        String email = authentication.getName();

        RecommendationResponse response = vendorService.addRecommendation(email, request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Recommendation added", response));
    }

    @PutMapping("/recommendations/{id}")
    public ResponseEntity<ApiResponse<RecommendationResponse>> updateRecommendation(@PathVariable Long id, Authentication authentication, @Valid @RequestBody RecommendationRequest request) {
        
        String email = authentication.getName();
        RecommendationResponse response = vendorService.updateRecommendation(id, email, request);

        return ResponseEntity.ok(
            ApiResponse.success("Recommdation updated", response)
        );
    }

    @DeleteMapping("/recommendations/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecommendation(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        vendorService.deleteRecommendation(id, email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendation deleted", null)
        );
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getMyRecommendations(Authentication authentication) {
        String email = authentication.getName();
        List<RecommendationResponse> recommendations = vendorService.getMyRecommendations(email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendations fetched", recommendations)
        );
    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getRecommendationById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        RecommendationResponse response = vendorService.getRecommendationById(id, email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendation fetched", response)
        );
    }
}
