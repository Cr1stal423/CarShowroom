package com.dealership.car.dynamic;

import com.dealership.car.repository.PersonRepository;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "dynamic_fields_value")
@Data
public class DynamicFieldValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private FieldsMetadata field;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "value")
    private String value;
}
