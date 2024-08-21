package com.dealership.car.dynamic;

import jakarta.persistence.*;
import lombok.*;

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
