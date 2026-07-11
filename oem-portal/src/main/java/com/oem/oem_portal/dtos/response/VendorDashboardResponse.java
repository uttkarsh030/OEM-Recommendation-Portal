package com.oem.oem_portal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDashboardResponse {
    private long totalUploads;
    private long assignedRecommendations;
    private long implementedRecommendations;
    private long verifiedRecommendations;
}
