    package com.dealership.car.model;

    import com.dealership.car.dynamic.IdentifiableEntity;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.*;

    import java.util.HashSet;
    import java.util.Set;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class Product extends BaseEntity implements IdentifiableEntity {
        @Id
        @GeneratedValue(strategy =  GenerationType.IDENTITY)
        private Integer productId;

        private String originCountry;

        private String brand;

        private String model;

        private String color;
        @Enumerated(EnumType.STRING)

        private AvailabilityStatus availabilityStatus;

        private Long price;
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "technical_id", referencedColumnName = "technicalId")
        private TechnicalData technicalData;
        @OneToMany(mappedBy = "product")
        private Set<OrderEntity> orders = new HashSet<>();

        @Override
        public Integer getId() {
            return productId;
        }

        public enum AvailabilityStatus{
            AVAILABLE,
            NOT_AVAILABLE,
            COMING_SOON
        }
    }
