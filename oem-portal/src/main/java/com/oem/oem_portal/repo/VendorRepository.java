package com.oem.oem_portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    Optional<Vendor> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

}
