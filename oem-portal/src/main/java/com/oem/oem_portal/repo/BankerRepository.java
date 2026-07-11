package com.oem.oem_portal.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Banker;
import com.oem.oem_portal.enums.BankerStatus;




public interface BankerRepository extends JpaRepository<Banker, Long> {
    
    Optional<Banker> findByEmail(String email);

    Optional<Banker> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Banker> findByStatus(BankerStatus status);

}