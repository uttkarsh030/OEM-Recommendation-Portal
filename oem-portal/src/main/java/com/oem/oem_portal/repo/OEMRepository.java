package com.oem.oem_portal.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.OEM;

public interface OEMRepository extends JpaRepository<OEM,Long>{
    Optional<OEM> findByName(String name);

    boolean existsByName(String name);

}
