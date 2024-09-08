package com.dealership.car.repository;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Integer> {

    public List<OrderEntity> findByPerson(Person person);

    public Optional<OrderEntity> findByProduct(Product product);

    public List<OrderEntity> findByPerson_Username(String username);

    public List<OrderEntity> findByPaymentType(String paymentType);
    @Query("SELECT DISTINCT o.paymentType FROM OrderEntity o")
    public List<String> findAllUniquePaymentTypes();
}
