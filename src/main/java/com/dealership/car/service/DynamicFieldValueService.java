package com.dealership.car.service;

import com.dealership.car.DTO.DynamicFieldDto;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.dynamic.FieldsMetadata;
import com.dealership.car.mapper.DynamicFieldMapper;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.repository.DynamicFieldValueRepository;
import com.dealership.car.repository.FieldMetadataRepository;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DynamicFieldValueService {

    private final PersonRepository personRepository;

    private DynamicFieldValueRepository dynamicFieldValueRepository;

    private FieldMetadataRepository fieldMetadataRepository;

    private ProductRepository productRepository;

    private DynamicFieldMapper dynamicFieldMapper;

    private final ApplicationContext context;

    @PersistenceContext
    private EntityManager entityManager;


    public List<DynamicFieldValue> getAllDynamicValueForEntity(Integer entityId, String entityType) {
        return dynamicFieldValueRepository.findAllByEntityIdAndEntityType(entityId, entityType);
    }

    public FieldsMetadata addFieldMetadata(FieldsMetadata fieldMetadata) {
        return fieldMetadataRepository.save(fieldMetadata);
    }

    public void saveDynamicFieldValue(DynamicFieldValue dynamicFieldValue) {
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

    //TODO make that with wildcard
    public String getEntityType(Integer entityId) {
        String className = "";
        Optional<Product> product = productRepository.findById(entityId);
        if (product.isPresent()) {
            Product product1 = product.get();
            Class<?> entityClass = product1.getClass();
            className = entityClass.getSimpleName();
        }
        return className;
    }
    public String getEntityType(Integer entityId, String entityType) {
        String className = "";
        try {
            // Динамічно завантажуємо клас сутності
            Class<?> entityClass = Class.forName("com.dealership.car.model." + entityType);

            // Виконуємо запит через EntityManager
            Object entity = entityManager.find(entityClass, entityId);

            if (entity != null) {
                className = entity.getClass().getSimpleName();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return className;
    }

    private String getRepositoryBeanName(Class<?> entityClass) {
        String entityName = entityClass.getSimpleName();
        return entityName + "Repository";
    }

    public DynamicFieldValue optionalFindById(Integer id) {
        Optional<DynamicFieldValue> optionalDynamicFieldValue = dynamicFieldValueRepository.findById(id);
        DynamicFieldValue dynamicFieldValue = new DynamicFieldValue();
        if (optionalDynamicFieldValue.isPresent()) {
            dynamicFieldValue = optionalDynamicFieldValue.get();
        } else {
            System.out.println("error, not found dynamic field ith given id");
        }
        return dynamicFieldValue;
    }

    public Integer findEntityIdByDynamicValueId(Integer dynamicFieldId) {
        Optional<DynamicFieldValue> dynamicFieldValue = dynamicFieldValueRepository.findById(dynamicFieldId);
        Integer entityId = 0;
        if (dynamicFieldValue.isPresent()) {
            entityId = dynamicFieldValue.get().getEntityId();
        }
        return entityId;
    }

    public DynamicFieldDto setDataToDto(Integer dynamicFieldValueId) {
        DynamicFieldValue dynamicFieldValue = optionalFindById(dynamicFieldValueId);
        FieldsMetadata fieldsMetadata = dynamicFieldValue.getField();
        DynamicFieldDto dynamicFieldDto = new DynamicFieldDto();
        dynamicFieldDto.setEntityId(dynamicFieldValue.getEntityId());
        dynamicFieldDto.setEntityType(dynamicFieldValue.getEntityType());
        dynamicFieldDto.setFieldId(fieldsMetadata.getId());
        dynamicFieldDto.setFieldName(fieldsMetadata.getFieldName());
        dynamicFieldDto.setFieldType(fieldsMetadata.getFieldType());
        dynamicFieldDto.setValue(dynamicFieldValue.getValue());

        return dynamicFieldDto;
    }

    public Boolean saveDynamicFieldsValueFromDto(DynamicFieldDto dynamicFieldDto) {
        Boolean isSaved = false;
        try {
            FieldsMetadata fieldsMetadata = dynamicFieldMapper.toSetFieldsMetadata(dynamicFieldDto);
            DynamicFieldValue dynamicFieldValue = dynamicFieldMapper.toSetDynamicValue(dynamicFieldDto);
            FieldsMetadata f = fieldMetadataRepository.save(fieldsMetadata);
            dynamicFieldValueRepository.save(dynamicFieldValue);
            isSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    public Boolean deleteDynamicValue(Integer dynamicValueId) {
        Boolean isDeleted = false;
        try {
            DynamicFieldValue dynamicFieldValue = optionalFindById(dynamicValueId);
            dynamicFieldValueRepository.delete(dynamicFieldValue);
            isDeleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public void deleteAllByList(List<DynamicFieldValue> dynamicFieldValueList) {
        dynamicFieldValueRepository.deleteAll(dynamicFieldValueList);

    }

//    public Contact getContactById(Integer entityId) {
//        return contactRepository.findById(entityId).orElse(null);
//    }
//
//    public Keys getKeysById(Integer entityId) {
//        return keysRepository.findById(entityId).orElse(null);
//    }
//    public OrderEntity getOrderById(Integer entityId){
//        return orderEntityRepository.findById(entityId).orElse(null);
//    }
//    public Person getPersonById(Integer entityId){
//        return personRepository.findById(entityId).orElse(null);
//    }
//    public Product getProductById(Integer entityId) {
//        return productRepository.findById(entityId).orElse(null);
//    }
//    public Roles getRoleById(Integer entityId){
//        return rolesRepository.findById(entityId).orElse(null);
//    }
//    public TechnicalData getTechnicalDataById(Integer entityId){
//        return technicalDataRepository.findById(entityId).orElse(null);
//    }

}
