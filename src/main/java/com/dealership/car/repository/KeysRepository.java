package com.dealership.car.repository;

import com.dealership.car.model.Keys;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface KeysRepository extends JpaRepository<Keys,Integer> {
}
