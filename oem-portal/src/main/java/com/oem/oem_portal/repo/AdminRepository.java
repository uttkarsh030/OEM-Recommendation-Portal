package com.oem.oem_portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    Optional<Admin> findByEmail(String email);

    boolean existsByEmail(String email);

}
