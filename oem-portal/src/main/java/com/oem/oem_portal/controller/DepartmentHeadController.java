package com.oem.oem_portal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oem.oem_portal.dtos.request.AssignRecommendationToBankerRequest;
import com.oem.oem_portal.dtos.response.ApiResponse;
import com.oem.oem_portal.dtos.response.BankerResponse;
import com.oem.oem_portal.dtos.response.DepartmentHeadDashboardResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.service.DepartmentHeadService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/department-head")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DEPARTMENT_HEAD')")
public class DepartmentHeadController {
    private final DepartmentHeadService departmentHeadService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DepartmentHeadDashboardResponse>> getDashboard(Authentication authentication) {
        String email = authentication.getName();

        DepartmentHeadDashboardResponse dashboard = departmentHeadService.getDashboard(email);

        return ResponseEntity.ok(
            ApiResponse.success("Dashboard loaded", dashboard)
        );
    }

    @GetMapping("/recommendations/assigned")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getAssignedRecommendations(Authentication authentication) {
        String email = authentication.getName();
        List<RecommendationResponse> recommendations = departmentHeadService.getAssignedRecommendations(email);

        return ResponseEntity.ok(
            ApiResponse.success("Assigned recommendation fetched", recommendations)
        );
    }

    @PutMapping("/recommendations/{id}/review")
    public ResponseEntity<ApiResponse<RecommendationResponse>> startReview(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();

        RecommendationResponse response = departmentHeadService.startReview(id, email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendation under review", response)
        );
    }

    @GetMapping("/bankers")
    public ResponseEntity<ApiResponse<List<BankerResponse>>> getMyBankers(Authentication authentication) {
        String email = authentication.getName();
        List<BankerResponse> bankers = departmentHeadService.getMyBankers(email);

        return ResponseEntity.ok(
            ApiResponse.success("Bankers fetched", bankers)
        );
    }

    @PostMapping("/recommendations/assign-banker")
    public ResponseEntity<ApiResponse<RecommendationResponse>> assignToBanker(Authentication authentication, @Valid @RequestBody AssignRecommendationToBankerRequest request) {

        String email = authentication.getName();

        RecommendationResponse response = departmentHeadService.assignToBanker(request, email);

        return ResponseEntity.ok(
            ApiResponse.success("Recommendation assigned to banker", response)
        );
    }

    @GetMapping("/recommendations/implemented")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getImplementedRecommendations(Authentication authentication) {
        String email = authentication.getName();

        List<RecommendationResponse> responses = departmentHeadService.getImplementedRecommendations(email);

        return ResponseEntity.ok(
            ApiResponse.success("Implemented recommendations fetched", responses)
        );
    }

    @PutMapping("/recommendations/{id}/review-implementation")
    public ResponseEntity<ApiResponse<RecommendationResponse>> reviewImplementation(@Valid @RequestBody Long id, Authentication authentication) {
        String email = authentication.getName();

        RecommendationResponse response = departmentHeadService.reviewImplementation(id, email);

        return ResponseEntity.ok(
            ApiResponse.success("Implementation reviewed", response)
        );
    }

}
