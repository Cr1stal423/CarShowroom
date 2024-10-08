package com.dealership.car.model;

import com.dealership.car.dynamic.IdentifiableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TechnicalData extends BaseEntity implements IdentifiableEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer technicalId;

    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "body type is required")
    private BodyType bodyType;
//    @NotBlank(message = "doors is required")
    private Integer doors;

    @OneToOne(mappedBy = "technicalData")
    private Product product;
//    @NotBlank(message = "seats is required")
    private Integer seats;
    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "engine type is required")
    private EngineType engineType;
    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "engine placement is required")
    private EnginePlacement enginePlacement;
//    @NotBlank(message = "engine capacity is required")
    private Double engineCapacity;

    @Override
    public Integer getId() {
        return technicalId;
    }


    public enum BodyType {
        SEDAN,
        COUPE,
        HATCHBACK,
        SUV,
        CONVERTIBLE,
        WAGON,
        VAN,
        TRUCK
    }

    public enum EngineType {
        GASOLINE,
        DIESEL,
        ELECTRIC,
        HYBRID,
        HYDROGEN
    }

    public enum EnginePlacement {
        FRONT,
        REAR,
        MID
    }



}



