package com.dealership.car.service;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class for managing technical data operations such as retrieving dynamic field values
 * and fetching technical data associated with a specific product model.
 */
@Service
public class TechnicalDataService {
    private final DynamicFieldValueService dynamicFieldValueService;
    private final PersonRepository personRepository;
    private final ProductRepository productRepository;
    private final TechnicalDataRepository technicalDataRepository;

    public TechnicalDataService(DynamicFieldValueService dynamicFieldValueService, PersonRepository personRepository, ProductRepository productRepository, TechnicalDataRepository technicalDataRepository) {
        this.dynamicFieldValueService = dynamicFieldValueService;
        this.personRepository = personRepository;
        this.productRepository = productRepository;
        this.technicalDataRepository = technicalDataRepository;
    }

    /**
     * Retrieves a map containing all technical data entries with their associated dynamic field values.
     *
     * @param technicalDataList a list of TechnicalData objects for which dynamic field values are to be retrieved.
     * @return a map where the key is a TechnicalData object and the value is a list of its associated DynamicFieldValue objects.
     */
    public Map<TechnicalData, List<DynamicFieldValue>> getDynamicFieldsForAllTechnicalData(List<TechnicalData> technicalDataList){
        Map<TechnicalData,List<DynamicFieldValue>>  technicalDataDynamicFieldsMap = new HashMap<>();
        for (TechnicalData technicalData : technicalDataList){
            List<DynamicFieldValue> dynamicFieldValueList =
                    dynamicFieldValueService.getAllDynamicValueForEntity(technicalData.getTechnicalId(),"TechnicalData");
            if (!dynamicFieldValueList.isEmpty()){
                technicalDataDynamicFieldsMap.put(technicalData,dynamicFieldValueList);
            } else {
                technicalDataDynamicFieldsMap.put(technicalData,new ArrayList<>());
            }
        }
        return technicalDataDynamicFieldsMap;
    }
    /**
     * Retrieves a list of TechnicalData entities based on the specified product model.
     *
     * @param model the product model for which to retrieve the technical data
     * @return a list of TechnicalData entities associated with the specified model
     */
    public List<TechnicalData> getTechnicalDataByModel(String model){
        List<TechnicalData> technicalDataList = new ArrayList<>();
        List<Product> products = productRepository.findByModel(model);
        for (Product product : products){
            Integer id = product.getTechnicalData().getTechnicalId();
            Optional<TechnicalData> technicalData = technicalDataRepository.findById(id);
            if (technicalData.isPresent()){
                technicalDataList.add(technicalData.get());
            }
        }
        return technicalDataList;
    }
}
