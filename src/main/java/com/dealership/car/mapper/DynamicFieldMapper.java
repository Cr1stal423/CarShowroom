package com.dealership.car.mapper;

import com.dealership.car.DTO.DynamicFieldDto;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.dynamic.FieldsMetadata;
import com.dealership.car.repository.DynamicFieldValueRepository;
import com.dealership.car.repository.FieldMetadataRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * DynamicFieldMapper is responsible for mapping dynamic field DTOs to their corresponding metadata and values.
 * The class interacts with repositories to fetch and update field metadata as well as dynamic field values.
 */
@Component
@Data
@AllArgsConstructor
public class DynamicFieldMapper {

    private FieldMetadataRepository fieldMetadataRepository;

    private DynamicFieldValueRepository dynamicFieldValueRepository;

    /**
     * Maps data from a DynamicFieldDto object to an existing FieldsMetadata
     * object if it is found in the repository, or logs an error if not found.
     *
     * @param dynamicFieldDto the DTO containing the dynamic field data to be mapped
     * @return the updated FieldsMetadata object if found, otherwise the
     *         returned value could produce an error as it proceeds with an empty Optional
     */
    public FieldsMetadata toSetFieldsMetadata(DynamicFieldDto dynamicFieldDto) {
        Optional<FieldsMetadata> fieldsMetadata = fieldMetadataRepository.findById(dynamicFieldDto.getFieldId());
        if (fieldsMetadata.isPresent()){
            fieldsMetadata.get().setFieldName(dynamicFieldDto.getFieldName());
            fieldsMetadata.get().setFieldType(dynamicFieldDto.getFieldType());
        } else {
            System.out.println("Error");
        }
        return fieldsMetadata.get();
    }

    /**
     * Updates and returns a dynamic field value based on the given DynamicFieldDto.
     * This method fetches the existing dynamic field values for the specified entityId
     * and entityType, then updates the relevant field value with the new data from
     * the provided DynamicFieldDto.
     *
     * @param dynamicFieldDto the DTO containing dynamic field data to update the existing field value
     * @return the updated DynamicFieldValue
     */
    public DynamicFieldValue toSetDynamicValue(DynamicFieldDto dynamicFieldDto){
        List<DynamicFieldValue> dynamicFieldValueList =
                dynamicFieldValueRepository.findAllByEntityIdAndEntityType(dynamicFieldDto.getEntityId(), dynamicFieldDto.getEntityType());

        dynamicFieldValueList.removeIf(dynamicFieldValue -> !Objects.equals(dynamicFieldValue.getField().getId(), dynamicFieldDto.getFieldId()));
        Optional<DynamicFieldValue> dynamicFieldValue = dynamicFieldValueRepository.findById(dynamicFieldValueList.get(0).getId());
        if (dynamicFieldValue.isPresent()){
            dynamicFieldValue.get().setValue(dynamicFieldDto.getValue());
            dynamicFieldValue.get().setEntityId(dynamicFieldDto.getEntityId());
            dynamicFieldValue.get().setEntityType(dynamicFieldDto.getEntityType());
        }
        return dynamicFieldValue.get();
    }
}
