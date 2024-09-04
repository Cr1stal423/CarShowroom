package com.dealership.car.service;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Product;
import com.dealership.car.repository.OrderEntityRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    private final DynamicFieldValueService dynamicFieldValueService;
    private final OrderEntityRepository orderEntityRepository;
    private final ProductService productService;

    public OrderService(DynamicFieldValueService dynamicFieldValueService, OrderEntityRepository orderEntityRepository, ProductService productService) {
        this.dynamicFieldValueService = dynamicFieldValueService;
        this.orderEntityRepository = orderEntityRepository;
        this.productService = productService;
    }

    public Map<OrderEntity, List<DynamicFieldValue>> getDynamicFieldsForAllOrder(List<OrderEntity> orderEntityList) {
        Map<OrderEntity, List<DynamicFieldValue>> orderDynamicFieldsMap = new HashMap<>();
        for (OrderEntity order : orderEntityList) {
            List<DynamicFieldValue> dynamicFieldValueList =
                    dynamicFieldValueService.getAllDynamicValueForEntity(order.getOrderId(), "OrderEntity");
            if (!dynamicFieldValueList.isEmpty()) {
                orderDynamicFieldsMap.put(order, dynamicFieldValueList);
            } else {
                orderDynamicFieldsMap.put(order, new ArrayList<>());
            }
        }
        return orderDynamicFieldsMap;
    }
    public OrderEntity findOrderByProduct(Product product){
        Optional<OrderEntity> order = orderEntityRepository.findByProduct(product);
        if (!order.isEmpty()){
            return order.get();
        } else {
            return new OrderEntity();
        }
    }
    public Long getSalesAmountForBrand(String brand){
        List<Product> products = productService.findProductByBrand(brand);
        List<OrderEntity> orderEntityList = new ArrayList<>();
        for (Product product: products){
            OrderEntity order = findOrderByProduct(product);
            orderEntityList.add(order);
        }
        Long result = (long) orderEntityList.size();
        return  result;
    }
    public Boolean deleteOrder(Integer orderId){
        Boolean isDeleted = false;
        Optional<OrderEntity> order = orderEntityRepository.findById(orderId);
        if (!order.isEmpty()){
            orderEntityRepository.delete(order.get());
            isDeleted = true;
        }
        return isDeleted;
    }

}
