package com.dealership.car.service;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Product;
import com.dealership.car.repository.OrderEntityRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The OrderService class provides various services related to orders.
 * It interacts with DynamicFieldValueService, OrderEntityRepository, and ProductService to manage orders and their associated dynamic fields.
 */
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

    /**
     * Retrieves dynamic field values for all orders in the given list.
     *
     * @param orderEntityList a list of OrderEntity objects for which dynamic field values are to be retrieved.
     * @return a map where the key is an OrderEntity and the value is a list of associated DynamicFieldValue objects.
     *         If no dynamic field values are found for an order, an empty list is associated with that order.
     */
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
    /**
     * Finds an order entity associated with the given product.
     *
     * @param product The product for which to find the associated order.
     * @return The order entity associated with the specified product, or a new order entity if no order is found.
     */
    public OrderEntity findOrderByProduct(Product product){
        Optional<OrderEntity> order = orderEntityRepository.findByProduct(product);
        if (!order.isEmpty()){
            return order.get();
        } else {
            return new OrderEntity();
        }
    }
    /**
     * Retrieves the sales amount for a given brand by counting the number of orders.
     *
     * @param brand the name of the brand for which to retrieve the sales amount
     * @return the number of orders associated with the specified brand as a Long value
     */
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
    /**
     * Deletes an order by its ID.
     *
     * @param orderId the ID of the order to delete
     * @return true if the order was successfully deleted, false otherwise
     */
    public Boolean deleteOrder(Integer orderId){
        Boolean isDeleted = false;
        Optional<OrderEntity> order = orderEntityRepository.findById(orderId);
        if (!order.isEmpty()){
            orderEntityRepository.delete(order.get());
            isDeleted = true;
        }
        return isDeleted;
    }
    /**
     * Retrieves a list of orders associated with the specified username.
     *
     * @param username the username associated with the orders to be retrieved.
     * @return a list of {@code OrderEntity} objects related to the given username.
     */
    public List<OrderEntity> userOrders(String username){
        List<OrderEntity> orders = orderEntityRepository.findByPerson_Username(username);
        return orders;
    }
    public List<Object[]> getTopSellingCarsPerQuarter() {
        return orderEntityRepository.findTopSellingCarsPerQuarter();
    }



}
