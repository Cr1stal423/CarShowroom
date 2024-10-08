package com.dealership.car.repository;

import com.dealership.car.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
    public List<Supplier> findByIsDelayed(Boolean isDelayed);
}
