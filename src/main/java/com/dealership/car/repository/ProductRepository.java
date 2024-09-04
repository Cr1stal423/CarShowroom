package com.dealership.car.repository;

import com.dealership.car.model.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Scope("singeton")
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findAllUniqueBrands();

    List<Product> findByBrand(String brand);

}
