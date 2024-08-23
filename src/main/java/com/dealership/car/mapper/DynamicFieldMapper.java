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

@Component
@Data
@AllArgsConstructor
public class DynamicFieldMapper {

    private FieldMetadataRepository fieldMetadataRepository;

    private DynamicFieldValueRepository dynamicFieldValueRepository;

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
