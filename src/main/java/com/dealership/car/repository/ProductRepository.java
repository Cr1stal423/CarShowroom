package com.dealership.car.repository;

import com.dealership.car.model.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Scope("singeton")
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
