package com.oem.oem_portal.dtos.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RecommendationRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "OEM is required")
    private String oemName;

    @NotBlank(message = "product name is required")
    private String productName;

    @NotBlank(message = "Application name is required")
    private String applicationName;

    private String version;

    private String releaseDate;

}
