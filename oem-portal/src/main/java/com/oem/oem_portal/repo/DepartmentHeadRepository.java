package com.oem.oem_portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.DepartmentHead;

public interface DepartmentHeadRepository extends JpaRepository<DepartmentHead, Long>{
    Optional<DepartmentHead> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
