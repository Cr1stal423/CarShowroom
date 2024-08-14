package com.dealership.car.repository;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer> {
    public List<OrderEntity> findByPerson(Person person);
}
