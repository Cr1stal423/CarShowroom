package com.dealership.car.service;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final DynamicFieldValueService dynamicFieldValueService;

    public OrderService(DynamicFieldValueService dynamicFieldValueService) {
        this.dynamicFieldValueService = dynamicFieldValueService;
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
}
