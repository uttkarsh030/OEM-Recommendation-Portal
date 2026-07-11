package com.oem.oem_portal.service;

import java.util.List;

import com.oem.oem_portal.dtos.request.AssignBankerRequest;
import com.oem.oem_portal.dtos.request.AssignDepartmentHeadRequest;
import com.oem.oem_portal.dtos.request.AssignRecommendationRequest;
import com.oem.oem_portal.dtos.request.DepartmentRequest;
import com.oem.oem_portal.dtos.response.AdminDashboardResponse;
import com.oem.oem_portal.dtos.response.BankerResponse;
import com.oem.oem_portal.dtos.response.DepartmentResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.dtos.response.VendorResponse;

public interface AdminService {
    
    AdminDashboardResponse getDashboard();

    DepartmentResponse createDepartment(DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    DepartmentResponse toggleDepartmentStatus(Long id);
    
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse assignDepartmentHead(AssignDepartmentHeadRequest request);

    List<BankerResponse> getPendingBankers();
    List<BankerResponse> getAllBankers();
    BankerResponse approveBanker(Long bankerId);
    BankerResponse rejectBanker(Long bankerId);
    BankerResponse assignBankerToDepartment(AssignBankerRequest request);

    List<VendorResponse> getAllVendors();

    List<RecommendationResponse> getAllRecommendations();
    List<RecommendationResponse> getUploadedRecommendations();
    RecommendationResponse assignRecommendationToDepartment(AssignRecommendationRequest request);

    List<RecommendationResponse> getReviewedRecommendations();
    RecommendationResponse verifyRecommendation(Long recommendationId);

}
