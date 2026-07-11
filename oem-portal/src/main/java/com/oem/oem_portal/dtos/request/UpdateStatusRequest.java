package com.oem.oem_portal.dtos.request;

import com.oem.oem_portal.enums.RecommendationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull(message = "status is required")
    private RecommendationStatus status;

}
