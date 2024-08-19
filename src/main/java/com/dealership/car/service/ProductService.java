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

        productRepository.save(product);

    }
}
