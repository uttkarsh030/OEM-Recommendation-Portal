package com.oem.oem_portal.service;

import java.util.List;

import com.oem.oem_portal.dtos.request.RecommendationRequest;
import com.oem.oem_portal.dtos.response.RecommendationResponse;
import com.oem.oem_portal.dtos.response.VendorDashboardResponse;

public interface VendorService {
    VendorDashboardResponse getDashboard(String email);

    RecommendationResponse addRecommendation(String email, RecommendationRequest request);

    RecommendationResponse updateRecommendation(Long id, String email, RecommendationRequest request);

    void deleteRecommendation(Long id, String email);

    List<RecommendationResponse> getMyRecommendations(String email);

    RecommendationResponse getRecommendationById(Long id, String email);
}
