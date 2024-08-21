package com.dealership.car.service;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.dynamic.FieldsMetadata;
import com.dealership.car.model.*;
import com.dealership.car.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DynamicFieldValueService {

    private DynamicFieldValueRepository dynamicFieldValueRepository;

    private FieldMetadataRepository fieldMetadataRepository;

    private ContactRepository contactRepository;

    private KeysRepository keysRepository;

    private OrderEntityRepository orderEntityRepository;

    private PersonRepository personRepository;

    private ProductRepository productRepository;

    private RolesRepository rolesRepository;

    private TechnicalDataRepository technicalDataRepository;
    public List<DynamicFieldValue> getAllDynamicValueForEntity(Integer entityId, String entityType){
        return dynamicFieldValueRepository.findAllByEntityIdAndEntityType(entityId,entityType);
    }
    // Метод для додавання нового динамічного поля
    public FieldsMetadata addFieldMetadata(FieldsMetadata fieldMetadata) {
        return fieldMetadataRepository.save(fieldMetadata);
    }
    public void saveDynamicFieldValue(DynamicFieldValue dynamicFieldValue){
        dynamicFieldValueRepository.save(dynamicFieldValue);
    }
    public Map<Integer, String> getDynamicValuesForEntity(Integer entityId, String entityType) {
        List<DynamicFieldValue> dynamicFieldValues = dynamicFieldValueRepository.findAllByEntityIdAndEntityType(entityId, entityType);
        Map<Integer, String> valuesMap = new HashMap<>();

        for (DynamicFieldValue value : dynamicFieldValues) {
            valuesMap.put(value.getField().getId(), value.getValue());
        }

        return valuesMap;
    }
    public List<FieldsMetadata> getAllFields() {
        return fieldMetadataRepository.findAll();
    }


    public Contact getContactById(Integer entityId) {
        return contactRepository.findById(entityId).orElse(null);
    }

    public Keys getKeysById(Integer entityId) {
        return keysRepository.findById(entityId).orElse(null);
    }
    public OrderEntity getOrderById(Integer entityId){
        return orderEntityRepository.findById(entityId).orElse(null);
    }
    public Person getPersonById(Integer entityId){
        return personRepository.findById(entityId).orElse(null);
    }
    public Product getProductById(Integer entityId) {
        return productRepository.findById(entityId).orElse(null);
    }
    public Roles getRoleById(Integer entityId){
        return rolesRepository.findById(entityId).orElse(null);
    }
    public TechnicalData getTechnicalDataById(Integer entityId){
        return technicalDataRepository.findById(entityId).orElse(null);
    }

}
