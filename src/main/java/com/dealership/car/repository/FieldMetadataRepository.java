package com.dealership.car.repository;

import com.dealership.car.dynamic.FieldsMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldMetadataRepository extends JpaRepository<FieldsMetadata,Integer> {
}
