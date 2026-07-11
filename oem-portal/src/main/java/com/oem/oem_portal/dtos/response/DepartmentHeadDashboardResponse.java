package com.oem.oem_portal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentHeadDashboardResponse {
    private long assignedRecommendations;
    private long underReviewRecommendations;
    private long assignedToBankers;
    private long implementedRecommendations;
    private long reviewedRecommendations;
    private String deaprtmentName;
}
