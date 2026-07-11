package com.oem.oem_portal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalDepartments;
    private long totalDepartmentHeads;
    private long totalBankers;
    private long pendingApprovals;
    private long totalVendors;

    // NEW FEILDS
    private long totalRecommendations;
    private long uploadedRecommendations;
    private long assignedRecommendations;
    private long reviewedRecommendations;
    private long verifiedRecommendations;
}
