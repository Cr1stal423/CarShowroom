package com.dealership.car.service;

import com.dealership.car.DTO.SupplierDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Product;
import com.dealership.car.model.Supplier;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final DynamicFieldValueService dynamicFieldValueService;
    private final ProductRepository productRepository;
    private final OrderEntityRepository orderEntityRepository;

    public void createSupplier(SupplierDto supplierDto) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierDto.getName());
        supplier.setCreatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }
    public Map<Supplier, List<DynamicFieldValue>> getAllDynamicFieldsForSupplier(List<Supplier> supplierList){
        Map<Supplier,List<DynamicFieldValue>> supplierDynamicFieldMap = new HashMap<>();
        for (Supplier supplier : supplierList){
            List<DynamicFieldValue> dynamicFieldValueList = dynamicFieldValueService.getAllDynamicValueForEntity(supplier.getId(),"Supplier");
            if (!dynamicFieldValueList.isEmpty()){
                supplierDynamicFieldMap.put(supplier,dynamicFieldValueList);
            } else {
                supplierDynamicFieldMap.put(supplier,new ArrayList<>());
            }
        }
        return supplierDynamicFieldMap;
    }
    public List<Supplier> getAllComingSoonSuppliers(){
        List<Product> products = productRepository.findByAvailabilityStatusEquals(Constants.AVAILABILITY_STATUSES.get(2));
        List<OrderEntity> orderList = new ArrayList<>();
        for (Product product : products){
            Optional<OrderEntity> orderEntity = orderEntityRepository.findByProduct(product);
            if (orderEntity.isPresent()){
                orderList.add(orderEntity.get());
            }
        }
        List<Supplier> supplierList = new ArrayList<>();
        for (OrderEntity order : orderList){
            Supplier supplier = order.getSupplier();
            if (supplier != null){
                supplierList.add(supplier);
            }
        }
        return supplierList;
    }
}
