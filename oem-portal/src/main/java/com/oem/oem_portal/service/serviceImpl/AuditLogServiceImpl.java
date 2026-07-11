package com.oem.oem_portal.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.oem.oem_portal.dtos.response.AuditLogResponse;
import com.oem.oem_portal.model.AuditLog;
import com.oem.oem_portal.repo.AudiLogRepository;
import com.oem.oem_portal.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AudiLogRepository audiLogRepository;

    public void log(String action, String performedBy, String role,
             Long recommendationId, String previousStatus,
             String newStatus, String details) {

                AuditLog auditLog = AuditLog.builder()
                    .action(action)
                    .performedBy(performedBy)
                    .role(role)
                    .recommendationId(recommendationId)
                    .previousStatus(previousStatus)
                    .newStatus(newStatus)
                    .details(details)
                    .timestamp(LocalDateTime.now())
                    .build();
                
                audiLogRepository.save(auditLog);
    }

    @Override
    public List<AuditLogResponse> getAllLogs() {
        return audiLogRepository.findAllByOrderByTimestampDesc()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public List<AuditLogResponse> getLogsByRecommendationId(Long recommendationId) {
        return audiLogRepository
            .findByRecommendationIdOrderByTimestampDesc(recommendationId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }


    @Override
    public List<AuditLogResponse> getLogsByUser(String email) {
        return audiLogRepository
            .findByPerformedByOrderByTimestampDesc(email)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }


    //private helper function
    private AuditLogResponse mapToResponse(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .action(log.getAction())
                .performedBy(log.getPerformedBy())
                .role(log.getRole())
                .recommendationId(log.getRecommendationId())
                .previousStatus(log.getPreviousStatus())
                .newStatus(log.getNewStatus())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}
