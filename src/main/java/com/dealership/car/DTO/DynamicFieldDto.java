package com.dealership.car.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
