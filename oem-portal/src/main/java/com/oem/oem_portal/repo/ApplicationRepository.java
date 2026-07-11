package com.oem.oem_portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long>{
    List<Application> findByProductId(Long productId);
}
