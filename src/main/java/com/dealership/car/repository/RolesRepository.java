package com.dealership.car.repository;

import com.dealership.car.model.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface RolesRepository extends JpaRepository<Roles,Integer> {
    @Transactional
    Roles findByRoleName(String roleName);
}
