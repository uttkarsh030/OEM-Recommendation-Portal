package com.oem.oem_portal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRecommendationToBankerRequest {
    @NotNull(message = "Recommendation Id is required")
    private Long recommendationId;

    @NotNull(message = "Bnaker Id is required")
    private Long bankerId;
}
