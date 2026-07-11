package com.oem.oem_portal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRecommendationRequest {
    @NotNull(message = "Recommdation ID is required")
    private Long recommendationId;

    @NotNull(message = "Department Id is required")
    private Long departmentId;
}
