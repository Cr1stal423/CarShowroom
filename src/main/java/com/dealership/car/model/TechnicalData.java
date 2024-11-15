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

    private BodyType bodyType;

    private Integer doors;

    @OneToOne(mappedBy = "technicalData")
    private Product product;

    private Integer seats;
    @Enumerated(EnumType.STRING)

    private EngineType engineType;
    @Enumerated(EnumType.STRING)

    private EnginePlacement enginePlacement;

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



