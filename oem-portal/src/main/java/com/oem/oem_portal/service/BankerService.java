package com.oem.oem_portal.service;

import java.util.List;

import com.oem.oem_portal.dtos.request.UpdateStatusRequest;
import com.oem.oem_portal.dtos.response.BankerDashboardResponse;
import com.oem.oem_portal.dtos.response.RecommendationResponse;

public interface BankerService {
    BankerDashboardResponse getDashboard(String email);

    List<RecommendationResponse> getAssignedRecommendations(String email);

    RecommendationResponse getRecommendationById(Long id, String email);

    RecommendationResponse updateStatus(Long recommendationId, UpdateStatusRequest request, String email);
}
