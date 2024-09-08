package com.dealership.car.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing a dynamic field.
 * This class is used to transfer data between different layers
 * of the application related to dynamic fields.
 *
 * The class contains attributes such as fieldName, fieldType, fieldId,
 * entityId, entityType, and value. Each of these fields is validated
 * using Bean Validation annotations to ensure that they meet the required
 * constraints when the object is created or modified.
 */
@Data
public class DynamicFieldDto {
    @NotBlank(message = "fieldName is required")
    private String fieldName;
    @NotBlank(message = "fieldType")
    private String fieldType;
    @NotNull(message = "field id is required")
    private Integer fieldId;
    @NotNull(message = "entityId is required")
    private Integer entityId;
    @NotBlank(message = "entityType is required")
    private String entityType;
    @NotBlank(message = "value is required")
    private String value;

}
