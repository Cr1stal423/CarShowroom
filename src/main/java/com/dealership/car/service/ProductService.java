package com.dealership.car.service;

import com.dealership.car.DTO.ProductDto;
import com.dealership.car.mapper.ProductMapper;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final TechnicalDataRepository technicalDataRepository;

    private final ProductMapper productMapper;

    @Transactional
    public void saveProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        TechnicalData technicalData = productMapper.toTechnicalData(productDto);


        product.setTechnicalData(technicalData);
        technicalData.setProduct(product);

        productRepository.save(product);
    }


    public boolean updateProduct(Integer productId, String originCountry, String brand, String model, String color,Product.AvailabilityStatus availabilityStatus ,Long price) {
       Optional<Product> optionalProduct = productRepository.findById(productId);
       boolean isSaved = false;
       if (optionalProduct.isPresent()){
           Product product = optionalProduct.get();
           product.setOriginCountry(originCountry);
           product.setBrand(brand);
           product.setModel(model);
           product.setColor(color);
           product.setAvailabilityStatus(availabilityStatus);
           product.setPrice(price);
           isSaved = true;

           productRepository.save(product);
       } else System.out.println(String.format("not found product with given id %s", productId));

       return isSaved;
    }

    public boolean updateTechData(Integer technicalId, TechnicalData.BodyType bodyType, Integer doors, Integer seats, TechnicalData.EngineType engineType, TechnicalData.EnginePlacement enginePlacement, Double engineCapacity) {
        boolean isSaved = false;
        Optional<TechnicalData> optionalData = technicalDataRepository.findById(technicalId);
        if (optionalData.isPresent()){
            TechnicalData technicalData = optionalData.get();
            technicalData.setBodyType(bodyType);
            technicalData.setDoors(doors);
            technicalData.setSeats(seats);
            technicalData.setEngineType(engineType);
            technicalData.setEnginePlacement(enginePlacement);
            technicalData.setEngineCapacity(engineCapacity);
            technicalData.setUpdatedAt(LocalDateTime.now());
            technicalData.setUpdatedBy("Operator");
            isSaved = true;

            technicalDataRepository.save(technicalData);
        } else System.out.println(String.format("not found technicalData with given id s", technicalId));
        return isSaved;
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }
}
