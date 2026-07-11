package com.oem.oem_portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Recommendation;
import com.oem.oem_portal.enums.RecommendationStatus;


public interface RecommendationRepository extends JpaRepository<Recommendation, Long>{
    List<Recommendation> findByVendorId(Long vendorId);

    List<Recommendation> findByDepartmentId(Long departmentId);

    List<Recommendation> findByBankerId(Long bankerId);

    List<Recommendation> findByStatus(RecommendationStatus status);

}
