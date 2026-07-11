package com.oem.oem_portal.dtos.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {
    private Long id;
    private String action;
    private String performedBy;
    private String role;
    private Long recommendationId;
    private String previousStatus;
    private String newStatus;
    private String details;
    private LocalDateTime timestamp;
}
