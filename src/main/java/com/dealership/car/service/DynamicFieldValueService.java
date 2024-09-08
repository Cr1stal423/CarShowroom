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

/**
 * DynamicFieldValueService handles operations related to dynamic field values,
 * field metadata, and entities associated with these dynamic fields. The class
 * provides methods to retrieve, save, and delete dynamic field values and metadata.
 */
@Service
@AllArgsConstructor
public class DynamicFieldValueService {

    private final PersonRepository personRepository;

    private DynamicFieldValueRepository dynamicFieldValueRepository;

    private FieldMetadataRepository fieldMetadataRepository;

    private ProductRepository productRepository;

    private DynamicFieldMapper dynamicFieldMapper;

    private final ApplicationContext context;

    /**
     * EntityManager instance used for interacting with the persistence context.
     * It is injected by the container and allows for CRUD operations on entities.
     * The EntityManager is responsible for managing the lifecycle of entity instances,
     * making it possible to create, read, update, and delete records in the database.
     */
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Retrieves all dynamic field values for a specific entity identified by its ID and type.
     *
     * @param entityId   the ID of the entity
     * @param entityType the type of the entity
     * @return a list of dynamic field values associated with the specified entity
     */
    public List<DynamicFieldValue> getAllDynamicValueForEntity(Integer entityId, String entityType) {
        return dynamicFieldValueRepository.findAllByEntityIdAndEntityType(entityId, entityType);
    }

    /**
     * Adds metadata for a dynamic field.
     *
     * @param fieldMetadata the metadata object containing information about the dynamic field to be added
     * @return the saved {@link FieldsMetadata} object
     */
    public FieldsMetadata addFieldMetadata(FieldsMetadata fieldMetadata) {
        return fieldMetadataRepository.save(fieldMetadata);
    }

    /**
     * Persists a dynamic field value to the database.
     *
     * @param dynamicFieldValue The dynamic field value to be saved.
     */
    public void saveDynamicFieldValue(DynamicFieldValue dynamicFieldValue) {
        dynamicFieldValueRepository.save(dynamicFieldValue);
    }

    /**
     * Retrieves dynamic field values for a specified entity by its ID and type.
     *
     * @param entityId the ID of the entity for which dynamic field values are to be retrieved
     * @param entityType the type of the entity for which dynamic field values are to be retrieved
     * @return a map where the key is the field ID and the value is the dynamic field value as a string
     */
    public Map<Integer, String> getDynamicValuesForEntity(Integer entityId, String entityType) {
        List<DynamicFieldValue> dynamicFieldValues = dynamicFieldValueRepository.findAllByEntityIdAndEntityType(entityId, entityType);
        Map<Integer, String> valuesMap = new HashMap<>();

        for (DynamicFieldValue value : dynamicFieldValues) {
            valuesMap.put(value.getField().getId(), value.getValue());
        }

        return valuesMap;
    }

    /**
     * Retrieves all fields metadata from the repository.
     *
     * @return a list of FieldsMetadata containing the metadata for all available fields.
     */
    public List<FieldsMetadata> getAllFields() {
        return fieldMetadataRepository.findAll();
    }

    /**
     * Retrieves the type of an entity based on its ID.
     * This method searches for the entity in the product repository
     * and, if found, returns the simple name of the entity's class.
     *
     * @param entityId the ID of the entity to find
     * @return the simple name of the entity's class, or an empty string if not found
     */
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
    /**
     * Retrieves the entity type as a simple class name based on the provided entity ID
     * and entity type. Performs a dynamic class loading and uses EntityManager to find
     * the entity instance.
     *
     * @param entityId the ID of the entity
     * @param entityType the type of the entity
     * @return the simple class name of the entity type if found, otherwise an empty string
     */
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

    /**
     * Generates the default repository bean name for the given entity class.
     *
     * @param entityClass the class type of the entity for which the repository bean name is being generated
     * @return the generated repository bean name for the specified entity class
     */
    private String getRepositoryBeanName(Class<?> entityClass) {
        String entityName = entityClass.getSimpleName();
        return entityName + "Repository";
    }

    /**
     * Finds a DynamicFieldValue instance by its ID in an optional manner. If the ID is present in the repository, the corresponding
     * DynamicFieldValue is returned. If the ID is not present, an empty DynamicFieldValue object is returned.
     *
     * @param id the ID of the DynamicFieldValue to find
     * @return the DynamicFieldValue object if found, otherwise an empty DynamicFieldValue object
     */
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

    /**
     * Finds the entity ID associated with the given dynamic field ID.
     *
     * @param dynamicFieldId the ID of the dynamic field to look up
     * @return the ID of the associated entity, or 0 if no such entity is found
     */
    public Integer findEntityIdByDynamicValueId(Integer dynamicFieldId) {
        Optional<DynamicFieldValue> dynamicFieldValue = dynamicFieldValueRepository.findById(dynamicFieldId);
        Integer entityId = 0;
        if (dynamicFieldValue.isPresent()) {
            entityId = dynamicFieldValue.get().getEntityId();
        }
        return entityId;
    }

    /**
     * Converts a dynamic field value identified by its ID into a DynamicFieldDto.
     *
     * @param dynamicFieldValueId the ID of the dynamic field value to be converted
     * @return the resulting DynamicFieldDto populated with the dynamic field's data
     */
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

    /**
     * Saves the dynamic field values from the provided DynamicFieldDto.
     * It maps the DTO to both FieldsMetadata and DynamicFieldValue, then saves these entities in their respective repositories.
     *
     * @param dynamicFieldDto the DTO containing the dynamic field data to be saved
     * @return true if the field metadata and value were saved successfully, false otherwise
     */
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

    /**
     * Deletes a dynamic value identified by the provided ID.
     *
     * @param dynamicValueId the ID of the dynamic value to be deleted
     * @return true if the deletion was successful, false otherwise
     */
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

    /**
     * Deletes all DynamicFieldValue entities provided in the list from the repository.
     *
     * @param dynamicFieldValueList the list of DynamicFieldValue entities to be deleted
     */
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
