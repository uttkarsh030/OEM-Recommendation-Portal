package com.oem.oem_portal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oem.oem_portal.dtos.request.UpdateStatusRequest;
import com.oem.oem_portal.dtos.response.ApiResponse;
import com.oem.oem_portal.dtos.response.BankerDashboardResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.service.BankerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banker")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BANKER')")
public class BankerController {

    private final BankerService bankerService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<BankerDashboardResponse>> getDashboard(Authentication authentication) {
        String email = authentication.getName();
        BankerDashboardResponse dashboard = bankerService.getDashboard(email);

        return ResponseEntity.ok(
            ApiResponse.success("Dashboard loaded", dashboard)
        );
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getAssignedRecommendations(Authentication authentication) {
        String email = authentication.getName();
        List<RecommendationResponse> recommendations = bankerService.getAssignedRecommendations(email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendations fetched", recommendations)
        );
    }

    @GetMapping("/recommendations/{id}")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getRecommendationById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        RecommendationResponse response = bankerService.getRecommendationById(id, email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendation fetched", response)
        );
    }

    @PutMapping("/recommendations/{id}/status")
    public ResponseEntity<ApiResponse<RecommendationResponse>> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request, Authentication authentication) {
        String email = authentication.getName();

        RecommendationResponse response = bankerService.updateStatus(id, request, email);

        return ResponseEntity.ok(
            ApiResponse.success("Status updated", response)
        );
    }
}
