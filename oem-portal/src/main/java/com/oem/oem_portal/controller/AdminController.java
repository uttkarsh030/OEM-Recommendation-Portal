package com.oem.oem_portal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oem.oem_portal.dtos.request.AssignBankerRequest;
import com.oem.oem_portal.dtos.request.AssignDepartmentHeadRequest;
import com.oem.oem_portal.dtos.request.AssignRecommendationRequest;
import com.oem.oem_portal.dtos.request.DepartmentRequest;
import com.oem.oem_portal.dtos.response.AdminDashboardResponse;
import com.oem.oem_portal.dtos.response.ApiResponse;
import com.oem.oem_portal.dtos.response.AuditLogResponse;
import com.oem.oem_portal.dtos.response.BankerResponse;
import com.oem.oem_portal.dtos.response.DepartmentResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.dtos.response.VendorResponse;
import com.oem.oem_portal.service.AdminService;
import com.oem.oem_portal.service.AuditLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final AuditLogService auditLogService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {

        AdminDashboardResponse dashboard = adminService.getDashboard();

        return ResponseEntity.ok(
            ApiResponse.success("Dashboard loaded", dashboard)
        );
    }

    @PostMapping("/departments")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = adminService.createDepartment(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Deaprtment created", response));

    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse response = adminService.updateDepartment(id, request);

        return ResponseEntity.ok(
            ApiResponse.success("Department updated", response)
        );
    }

    @PutMapping("/departments/{id}/toggle-status")
    public ResponseEntity<ApiResponse<DepartmentResponse>> toggleDepartmentStatus(
            @PathVariable Long id
    ) {
        DepartmentResponse response = adminService.toggleDepartmentStatus(id);
        return ResponseEntity.ok(
            ApiResponse.success("Department status toggled", response)
        );
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> departments = adminService.getAllDepartments();

        return ResponseEntity.ok(
            ApiResponse.success("Departments fetched", departments)
        );
    }

    @PostMapping("/departments/assign-head")
    public ResponseEntity<ApiResponse<DepartmentResponse>> assignDepartmentHead(@Valid @RequestBody AssignDepartmentHeadRequest request) {
        DepartmentResponse response = adminService.assignDepartmentHead(request);

        return ResponseEntity.ok(
            ApiResponse.success("Department Head assigned", response)
        );
    }

    @GetMapping("/bankers/pending")
    public ResponseEntity<ApiResponse<List<BankerResponse>>> getPendingBankers() {
        List<BankerResponse> bankers = adminService.getPendingBankers();

        return ResponseEntity.ok(
            ApiResponse.success("Pending bankers fetched", bankers)
        );
    }

    @GetMapping("/bankers")
    public ResponseEntity<ApiResponse<List<BankerResponse>>> getAllBankers() {
        List<BankerResponse> bankers = adminService.getAllBankers();

        return ResponseEntity.ok(
            ApiResponse.success("All bankers fetched", bankers)
        );
    }

    @PutMapping("/bankers/{id}/approve")
    public ResponseEntity<ApiResponse<BankerResponse>> approveBanker(@PathVariable Long id) {
        BankerResponse response = adminService.approveBanker(id);

        return ResponseEntity.ok(
            ApiResponse.success("Banker approved", response)
        );
    }

    @PutMapping("/bankers/{id}/reject")
    public ResponseEntity<ApiResponse<BankerResponse>> rejectBanker(@PathVariable Long id) {
        BankerResponse response = adminService.rejectBanker(id);

        return ResponseEntity.ok(
            ApiResponse.success("Banker rejected", response)
        );
    }

    @PostMapping("/bankers/assign-department")
    public ResponseEntity<ApiResponse<BankerResponse>> assignBankerToDepartment(@Valid @RequestBody AssignBankerRequest request) {
        BankerResponse response = adminService.assignBankerToDepartment(request);

        return ResponseEntity.ok(
            ApiResponse.success("Banker assigned to a deaprtment", response)
        );
    }

    @GetMapping("/vendors")
    public ResponseEntity<ApiResponse<List<VendorResponse>>> getAllVendors() {
        List<VendorResponse> vendors = adminService.getAllVendors();
        return ResponseEntity.ok(
            ApiResponse.success("Vendor fetched", vendors)
        );
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getAllRecommendations() {
        List<RecommendationResponse> recommendations = 
            adminService.getAllRecommendations();

            return ResponseEntity.ok(
                ApiResponse.success("Recommendation fetched", recommendations)
            );
    }

    @GetMapping("/recommendations/uploaded")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getUploadedRecommendations() {
        List<RecommendationResponse> recommendations =
            adminService.getUploadedRecommendations();
        return ResponseEntity.ok(
            ApiResponse.success("Uploaded recommendations fetched", recommendations)
        );
    }

    @PostMapping("/recommendations/assign")
    public ResponseEntity<ApiResponse<RecommendationResponse>> assignRecommendation(
            @Valid @RequestBody AssignRecommendationRequest request
    ) {
        RecommendationResponse response =
            adminService.assignRecommendationToDepartment(request);
        return ResponseEntity.ok(
            ApiResponse.success("Recommendation assigned to department", response)
        );
    }

    @GetMapping("/recommendations/reviewed")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getReviewedRecommendations() {
        List<RecommendationResponse> recommendations =
            adminService.getReviewedRecommendations();
        return ResponseEntity.ok(
            ApiResponse.success("Reviewed recommendations fetched", recommendations)
        );
    }

    @PutMapping("/recommendations/{id}/verify")
    public ResponseEntity<ApiResponse<RecommendationResponse>> verifyRecommendation(
            @PathVariable Long id
    ) {
        RecommendationResponse response =
            adminService.verifyRecommendation(id);
        return ResponseEntity.ok(
            ApiResponse.success("Recommendation verified", response)
        );
    }



    //Audit LogsEndpoints
    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAllLogs() {
        List<AuditLogResponse> logs = auditLogService.getAllLogs();
        return ResponseEntity.ok(
            ApiResponse.success("Audit logs fetched", logs)
        );
    }

    @GetMapping("/audit-logs/recommendation/{id}")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogsByRecommendation(
            @PathVariable Long id
    ) {
        List<AuditLogResponse> logs = auditLogService.getLogsByRecommendationId(id);
        return ResponseEntity.ok(
            ApiResponse.success("Recommendation audit logs fetched", logs)
        );
    }

    @GetMapping("/audit-logs/user/{email}")
    public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getLogsByUser(
            @PathVariable String email
    ) {
        List<AuditLogResponse> logs = auditLogService.getLogsByUser(email);
        return ResponseEntity.ok(
            ApiResponse.success("User audit logs fetched", logs)
        );
    }

}
