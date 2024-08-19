package com.dealership.car.mapper;

import com.dealership.car.DTO.ProductDto;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class ProductMapper {

    public Product toProduct(ProductDto productDto){
        Product product = new Product();
        product.setOriginCountry(productDto.getOriginCountry());
        product.setBrand(productDto.getBrand());
        product.setModel(productDto.getModel());
        product.setColor(productDto.getColor());
        product.setAvailabilityStatus(productDto.getAvailabilityStatus());
        product.setPrice(productDto.getPrice());

        return product;
    }
    public TechnicalData toTechnicalData(ProductDto productDto){
        TechnicalData technicalData = new TechnicalData();
        technicalData.setBodyType(productDto.getBodyType());
        technicalData.setDoors(productDto.getDoors());
        technicalData.setSeats(productDto.getSeats());
        technicalData.setEngineType(productDto.getEngineType());
        technicalData.setEnginePlacement(productDto.getEnginePlacement());
        technicalData.setEngineCapacity(productDto.getEngineCapacity());
        technicalData.setCreatedAt(LocalDateTime.now());
        technicalData.setCreatedBy("Staff");

        return technicalData;
    }
}
