package com.dealership.car.service;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for performing analytics operations on products and orders.
 */
@Service
public class AnalyticsService {

    private final ProductRepository productRepository;
    private final OrderEntityRepository orderEntityRepository;
    private final OrderService orderService;

    /**
     * Constructs an AnalyticsService instance with the specified dependencies.
     *
     * @param productRepository         Repository for performing CRUD operations on Product entities.
     * @param orderEntityRepository     Repository for performing CRUD operations on OrderEntity entities.
     * @param orderService              Service for handling operations related to orders.
     */
    public AnalyticsService(ProductRepository productRepository, OrderEntityRepository orderEntityRepository, OrderService orderService) {
        this.productRepository = productRepository;
        this.orderEntityRepository = orderEntityRepository;
        this.orderService = orderService;
    }

    /**
     * Retrieves a list of all unique product brands from the product repository.
     *
     * @return a List of unique brand names as Strings.
     */
    public List<String> findAllUniqueBrand() {
        List<String> brands = productRepository.findAllUniqueBrands();
        return brands;
    }

    /**
     * Finds all orders associated with a list of products, ordered by brand.
     *
     * @param productByBrand the list of products filtered by brand
     * @return a list of order entities associated with the given list of products
     */
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

    /**
     * Retrieves the sales count for each brand provided in the list.
     *
     * @param brands the list of brand names for which to retrieve sales counts
     * @return a map where each key is a brand name and each value is the corresponding sales count
     */
    public Map<String, Long> getSalesCountByBrand(List<String> brands) {
        Map<String, Long> resultMap = new HashMap<>();
        Long amount;
        for (String brand : brands) {
            amount = orderService.getSalesAmountForBrand(brand);
            resultMap.put(brand, amount);
        }
        return resultMap;
    }

    /**
     * Calculates the total sales amount from the given map of sales data.
     *
     * @param map a map where the key is the brand name (String) and the value is the sales amount (Long)
     * @return the total sales amount as a Long
     */
    public Long getTotalSales(Map<String, Long> map) {
        Long totalSalesAmount = 0L;
        for (Long sales : map.values()) {
            totalSalesAmount += sales;
        }
        return totalSalesAmount;
    }
    /**
     * Retrieves a list of all unique product models from the product repository.
     *
     * @return a list containing unique product models
     */
    public List<String> getAllUniqueModel(){
        List<String> models = productRepository.findAllUniqueModels();
        return models;
    }

    public List<Product> findProductByPaymentType(String paymentType){
        List<Product> products = new ArrayList<>();
        List<OrderEntity> orderEntityList = orderEntityRepository.findByPaymentType(OrderEntity.PaymentType.valueOf(paymentType));
        for (OrderEntity orderEntity : orderEntityList){
            products.add(orderEntity.getProduct());
        }
        return products;
    }
    public List<Person> findUserByPaymentType(String paymentType){
        List<Person> persons = new ArrayList<>();
        List<OrderEntity> orderEntityList = orderEntityRepository.findByPaymentType(OrderEntity.PaymentType.valueOf(paymentType));
        for (OrderEntity orderEntity : orderEntityList){
            persons.add(orderEntity.getPerson());
        }
        return persons;
    }
    public Map<Integer, Integer> personAndProductByPaymentType(String paymentType) {
        Map<Integer, Integer> personAndProductMap = new HashMap<>();
        List<Person> personList = findUserByPaymentType(paymentType);
        List<Product> productList = findProductByPaymentType(paymentType);

        if (personList == null || productList == null) {
            throw new IllegalStateException("Person list or product list is null");
        }

        int i = 0;
        while (productList.size() > i && personList.size() > i) {
            int personId = personList.get(i).getId();
            int productId = productList.get(i).getId();
            personAndProductMap.put(personId, productId);
            i++;
        }

        return personAndProductMap;
    }
}
