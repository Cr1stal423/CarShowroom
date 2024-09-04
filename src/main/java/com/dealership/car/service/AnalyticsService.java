package com.dealership.car.service;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Product;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final OrderEntityRepository orderEntityRepository;
    private final OrderService orderService;

    public AnalyticsService(ProductRepository productRepository, OrderEntityRepository orderEntityRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderEntityRepository = orderEntityRepository;
        this.orderService = orderService;
    }

    public List<String> findAllUniqueBrand() {
        List<String> brands = productRepository.findAllUniqueBrands();
        return brands;
    }

    public List<OrderEntity> findAllOrderByBrand(List<Product> productByBrand) {
        List<OrderEntity> orderEntityList = new ArrayList<>();
        for (Product product : productByBrand) {
            Optional<OrderEntity> tempProduct = orderEntityRepository.findByProduct(product);
            if (!tempProduct.isEmpty()) {
                orderEntityList.add(tempProduct.get());
            }
        }
        return orderEntityList;
    }

    public Map<String, Long> getSalesCountByBrand(List<String> brands) {
        Map<String, Long> resultMap = new HashMap<>();
        Long amount;
        for (String brand : brands) {
            amount = orderService.getSalesAmountForBrand(brand);
            resultMap.put(brand, amount);
        }
        return resultMap;
    }

    public Long getTotalSales(Map<String, Long> map) {
        Long totalSalesAmount = 0L;
        for (Long sales : map.values()) {
            totalSalesAmount += sales;
        }
        return totalSalesAmount;
    }
    public List<String> getAllUniqueModel(){
        List<String> models = productRepository.findAllUniqueModels();
        return models;
    }
}
