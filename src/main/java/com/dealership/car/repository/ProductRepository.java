package com.dealership.car.repository;

import com.dealership.car.constants.Constants;
import com.dealership.car.model.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findAllUniqueBrands();

    List<Product> findByBrand(String brand);

    List<Product> findByAvailabilityStatusEquals(Product.AvailabilityStatus availabilityStatus);

    List<Product> findByModel(String model);

    List<Product> findByBrandAndModel(String brand,String model);

    @Query("SELECT DISTINCT p.model FROM Product p")
    List<String> findAllUniqueModels();


}
