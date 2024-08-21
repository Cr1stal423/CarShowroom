package com.dealership.car.repository;

import com.dealership.car.dynamic.DynamicFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicFieldValueRepository extends JpaRepository<DynamicFieldValue,Integer> {
    List<DynamicFieldValue> findAllByEntityIdAndEntityType(Integer entityId, String entityType);
}
