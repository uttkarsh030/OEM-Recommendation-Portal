package com.oem.oem_portal.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oem.oem_portal.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByOemId(Long oemId);
}
