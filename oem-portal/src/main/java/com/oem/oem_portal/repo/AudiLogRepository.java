package com.oem.oem_portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.oem.oem_portal.model.AuditLog;

public interface AudiLogRepository extends JpaRepository<AuditLog, Long>{
    
    List<AuditLog> findByRecommendationIdOrderByTimestampDesc(Long recommendationId);

    List<AuditLog> findByPerformedByOrderByTimestampDesc(String performedBy);

    List<AuditLog> findAllByOrderByTimestampDesc();

}
