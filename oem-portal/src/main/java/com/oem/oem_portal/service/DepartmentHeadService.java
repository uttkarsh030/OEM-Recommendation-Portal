package com.oem.oem_portal.service;

import java.util.List;

import com.oem.oem_portal.dtos.request.AssignRecommendationToBankerRequest;
import com.oem.oem_portal.dtos.response.BankerResponse;
import com.oem.oem_portal.dtos.response.DepartmentHeadDashboardResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;

public interface DepartmentHeadService {
    
    DepartmentHeadDashboardResponse getDashboard(String email);

    List<RecommendationResponse> getAssignedRecommendations(String email);

    RecommendationResponse startReview(Long recommendationId, String email);

    List<BankerResponse> getMyBankers(String email);

    RecommendationResponse assignToBanker(AssignRecommendationToBankerRequest request, String email);

    List<RecommendationResponse> getImplementedRecommendations(String email);

    RecommendationResponse reviewImplementation(Long recommendationId, String email);
}
