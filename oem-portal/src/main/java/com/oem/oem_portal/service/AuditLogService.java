package com.oem.oem_portal.service;

import java.util.List;

import com.oem.oem_portal.dtos.response.AuditLogResponse;

public interface AuditLogService {

    void log(String action, String performedBy, String role,
             Long recommendationId, String previousStatus,
             String newStatus, String details);

    List<AuditLogResponse> getAllLogs();

    List<AuditLogResponse> getLogsByRecommendationId(Long recommendationId);

    List<AuditLogResponse> getLogsByUser(String email);
}
