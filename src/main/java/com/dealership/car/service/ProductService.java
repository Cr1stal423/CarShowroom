package com.dealership.car.service;

import com.dealership.car.DTO.ProductDto;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.mapper.ProductMapper;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service class responsible for handling business logic related to products.
 * This class provides methods for saving, updating, finding, and deleting product entities and their associated data.
 */
@Service
@Data
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final TechnicalDataRepository technicalDataRepository;

    private final ProductMapper productMapper;

    private final DynamicFieldValueService dynamicFieldValueService;
    private final HttpSession httpSession;

    /**
     * Saves a new product to the repository.
     *
     * @param productDto The data transfer object containing the product details.
     */
    @Transactional
    public void saveProduct(ProductDto productDto) {
        Product product = productMapper.toProduct(productDto);
        TechnicalData technicalData = productMapper.toTechnicalData(productDto);


        product.setTechnicalData(technicalData);
        product.setCreatedAt(LocalDateTime.now());
        product.setCreatedBy("User");
        technicalData.setProduct(product);

        productRepository.save(product);
    }


    /**
     * Updates the details of an existing product identified by its productId.
     *
     * @param productId the unique identifier of the product to be updated
     * @param originCountry the new origin country of the product
     * @param brand the new brand of the product
     * @param model the new model of the product
     * @param color the new color of the product
     * @param availabilityStatus the new availability status of the product
     * @param price the new price of the product
     * @return true if the product is found and updated, false otherwise
     */
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

    /**
     * Updates the technical data of a product based on the provided parameters.
     *
     * @param technicalId The ID of the technical data to update.
     * @param bodyType The body type of the technical data.
     * @param doors The number of doors of the technical data.
     * @param seats The number of seats of the technical data.
     * @param engineType The engine type of the technical data.
     * @param enginePlacement The placement of the engine in the technical data.
     * @param engineCapacity The capacity of the engine in the technical data.
     * @return true if the technical data is updated successfully; false otherwise.
     */
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
    /**
     * Retrieves all Product entities from the repository.
     *
     * @return a list of all products
     */
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    /**
     * Retrieves the dynamic fields for each product in a given list.
     *
     * @param products a list of products for which the dynamic fields are to be retrieved
     * @return a map where each key is a product and the corresponding value is a list of dynamic field values for that product
     */
    public Map<Product,List<DynamicFieldValue>> getDynamicFieldsForAllProduct(List<Product> products){
        Map<Product,List<DynamicFieldValue>> productDynamicFieldMap = new HashMap<>();
        for (Product product : products){
            List<DynamicFieldValue> dynamicFieldValueList = dynamicFieldValueService.getAllDynamicValueForEntity(product.getProductId(),"Product");
            if (!dynamicFieldValueList.isEmpty()){
                productDynamicFieldMap.put(product,dynamicFieldValueList);
            } else {
                productDynamicFieldMap.put(product,new ArrayList<>());
            }
        }
        return productDynamicFieldMap;
    }

    /**
     * Deletes a product and its associated dynamic field values from the repository based on the given product ID.
     *
     * @param id the ID of the product to be deleted
     * @return {@code true} if the product and associated dynamic field values were successfully deleted, {@code false} otherwise
     */
    public Boolean deleteProductById(Integer id) {
        boolean isDeleted = false;
        Optional<Product> optionalProduct = productRepository.findById(id);
        List<DynamicFieldValue> dynamicFieldValueList =
                dynamicFieldValueService.getAllDynamicValueForEntity(id,dynamicFieldValueService.getEntityType(id, (String) httpSession.getAttribute("entityType")));
        if (optionalProduct.isPresent()){
            productRepository.delete(optionalProduct.get());
            dynamicFieldValueService.deleteAllByList(dynamicFieldValueList);
            isDeleted = true;
        }
        return isDeleted;
    }
    /**
     * Finds all products associated with the specified brand.
     *
     * @param brand the brand name used to filter products
     * @return a list of products that match the given brand name
     */
    public List<Product> findProductByBrand(String brand){
        List<Product> productByBrandList = productRepository.findByBrand(brand);
        return productByBrandList;
    }

}
