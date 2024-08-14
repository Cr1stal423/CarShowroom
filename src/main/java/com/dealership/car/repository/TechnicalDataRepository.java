package com.dealership.car.repository;

import com.dealership.car.model.TechnicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalDataRepository extends JpaRepository<TechnicalData,Integer> {
}
