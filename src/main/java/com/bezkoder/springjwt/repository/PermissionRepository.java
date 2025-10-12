package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    
    List<Permission> findByIsActiveTrue();
    
    Permission findByCode(String code);
}
