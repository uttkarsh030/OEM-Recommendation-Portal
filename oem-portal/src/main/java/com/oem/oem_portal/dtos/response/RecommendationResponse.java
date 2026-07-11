package com.oem.oem_portal.dtos.response;

import java.time.LocalDateTime;

import com.oem.oem_portal.enums.RecommendationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
    private Long id;
    private String title;
    private String description;
    private String oemName;
    private String productName;
    private String applicationName;
    private String version;
    private String releaseDate;
    private String documentPath;
    private LocalDateTime uploadDate;
    private RecommendationStatus status;
    private String vendorName;
    private String deapartmentName;
    private String bankerName;
}
