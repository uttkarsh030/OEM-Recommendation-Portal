package com.oem.oem_portal.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Department;

public interface DepartmentRepository extends JpaRepository<Department,Long>{
    Optional<Department> findByName(String name);

    boolean existsByName(String name);

    List<Department> findByActiveTrue();
}
