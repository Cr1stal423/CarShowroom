package com.dealership.car.dynamic;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the metadata for dynamic fields in the system.
 * This class is used to define the properties and types of fields
 * that can be dynamically added to entities.
 */
@Entity
@Table(name = "fields_metadata")
@Data
public class FieldsMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fieldName;

    private String fieldType;
}
