package com.dealership.car.service;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.ProductRepository;
import jakarta.transaction.Transactional;
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
    private final PersonService personService;
    private final ProductService productService;

    /**
     * Constructs an AnalyticsService instance with the specified dependencies.
     *
     * @param productRepository         Repository for performing CRUD operations on Product entities.
     * @param orderEntityRepository     Repository for performing CRUD operations on OrderEntity entities.
     * @param orderService              Service for handling operations related to orders.
     */
    public AnalyticsService(ProductRepository productRepository, OrderEntityRepository orderEntityRepository, OrderService orderService, PersonService personService, ProductService productService) {
        this.productRepository = productRepository;
        this.orderEntityRepository = orderEntityRepository;
        this.orderService = orderService;
        this.personService = personService;
        this.productService = productService;
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
//    public Map<Integer, Integer> personAndProductByPaymentType(String paymentType) {
//        Map<Integer, Integer> personAndProductMap = new HashMap<>();
//        List<Person> personList = findUserByPaymentType(paymentType);
//        List<Product> productList = findProductByPaymentType(paymentType);
//
//        if (personList == null || productList == null) {
//            throw new IllegalStateException("Person list or product list is null");
//        }
//
//        int i = 0;
//        while (productList.size() > i && personList.size() > i) {
//            int personId = personList.get(i).getId();
//            int productId = productList.get(i).getId();
//            personAndProductMap.put(personId, productId);
//            i++;
//        }
//
//        return personAndProductMap;
//    }
    public Map<Map<Person,List<DynamicFieldValue>>,Map<Product,List<DynamicFieldValue>>> personAndProductByPaymentType(String paymentType) {
        Map<Map<Person,List<DynamicFieldValue>>,Map<Product,List<DynamicFieldValue>>> personAndProductMap = new HashMap<>();
        List<Person> personList = findUserByPaymentType(paymentType);
        List<Product> productList = findProductByPaymentType(paymentType);
        Map<Person,List<DynamicFieldValue>> personMap = personService.getDynamicFieldsForAllPerson(personList);
        Map<Product,List<DynamicFieldValue>> productMap = productService.getDynamicFieldsForAllProduct(productList);
        personAndProductMap.put(personMap,productMap);

        return personAndProductMap;
    }

    public List<OrderEntity> getOrdersByProduct(List<Product> products){
        List<OrderEntity> orderEntityList = new ArrayList<>();
        for (Product product : products) {
            Optional<OrderEntity> tempProduct = orderEntityRepository.findByProduct(product);
            if (!tempProduct.isEmpty()) {
                orderEntityList.add(tempProduct.get());
            }
        }
        return orderEntityList;
    }

    public Integer totalAvailableCar(){
        List<Product> products = productRepository.findByAvailabilityStatusEquals(Constants.AVAILABILITY_STATUSES.get(0));
        return products.size();
    }
    public Map<String,Integer> totalAvailableCarByBrand(){
        Map<String,Integer> resultMap = new HashMap<>();
        List<Product> products = productRepository.findByAvailabilityStatusEquals(Constants.AVAILABILITY_STATUSES.get(0));
        for (Product product : products) {
            String brand = product.getBrand();
            if (resultMap.containsKey(brand)){
                resultMap.put(brand,resultMap.get(brand)+1);
            }else{
                resultMap.put(brand,1);
            }
        }
        return resultMap;
    }
    public Map<String,Integer> findLowStockCar(Map<String,Integer> totalAvailableCarByBrand){
        Map<String,Integer> resultMap = new HashMap<>();
        Integer totalAvailableCar = totalAvailableCar();
        List<String> allBrand = findAllUniqueBrand();
        Integer midCount = totalAvailableCar/allBrand.size();
        for (Map.Entry<String,Integer> entry : totalAvailableCarByBrand.entrySet()){
            if (entry.getValue()<midCount){
                resultMap.put(entry.getKey(),entry.getValue());
            }
        }
        return resultMap;
    }
    public Map<String, Integer> findLowStockCarByModel(List<String> carModels) {
        Map<String, Integer> modelCountMap = new HashMap<>();
        int totalCarForSelectedModels = 0;
        for (String carModel : carModels) {
            List<Product> products = productRepository.findByModel(carModel);
            int productCount = products.size();
            modelCountMap.put(carModel, productCount);
            totalCarForSelectedModels += productCount;
        }
        int averageCount = totalCarForSelectedModels / carModels.size();
        Map<String, Integer> resultMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : modelCountMap.entrySet()) {
            if (entry.getValue() < averageCount) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        }

        return resultMap;
    }
    public Map<Person,List<OrderEntity>> getOrdersByWorker(List<Person> workers){
        Map<Person,List<OrderEntity>> orderEntityMap = new HashMap<>();
        for (Person worker : workers) {
            List<OrderEntity> orderEntityList = orderEntityRepository.findByPerson(worker);
            Integer orderCount = orderEntityList.size();
            orderEntityMap.put(worker,orderEntityList);
        }
        return orderEntityMap;
    }
    @Transactional
    public Map<Person, Integer> countOrdersByWorker(List<Person> workers) {
        Map<Person, Integer> orderEntityMap = new HashMap<>();
        for (Person worker : workers) {
            List<OrderEntity> orderEntityList = orderEntityRepository.findByCreatedBy(worker.getUsername());
            Integer orderCount = orderEntityList.size();
            orderEntityMap.put(worker, orderCount);
        }
        return orderEntityMap;
    }
//public Map<Person, Integer> countOrdersByWorker(List<Person> workers) {
//    if (workers == null) {
//        throw new IllegalArgumentException("Worker list cannot be null");
//    }
//
//    Map<Person, Integer> orderEntityMap = new HashMap<>();
//
//    // Optimize database call to fetch all orders in one go
//    List<OrderEntity> allOrders = orderEntityRepository.findAllByPersonIn(workers);
//
//    // Count orders for each worker
//    for (Person worker : workers) {
//        long orderCount = allOrders.stream()
//                .filter(order -> order.getPerson().equals(worker))
//                .count();
//        orderEntityMap.put(worker, (int) orderCount);
//    }
//
//    return orderEntityMap;
//}
}
