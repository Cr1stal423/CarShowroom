    package com.dealership.car.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.Data;

    import java.util.HashSet;
    import java.util.Set;

    @Data
    @Entity
    public class Product {
        @Id
        @GeneratedValue(strategy =  GenerationType.IDENTITY)
        private Integer productId;
//        @NotBlank(message = "origin country required")
//        @Size(min = 2, message = "origin country must have at list 2 char")
        private String originCountry;
//        @NotBlank(message = "brand is required")
//        @Size(min = 3, message = "brand must have at list 3 char")
        private String brand;
//        @NotBlank(message = "model is required")
//        @Size(min = 2, message = "model must have at list 2 char")
        private String model;
//        @NotBlank(message = "color is required")
//        @Size(min = 2, message = "color must have at list 2 char")
        private String color;
        @Enumerated(EnumType.STRING)
//        @NotBlank(message = "Availability Status is required")
        private AvailabilityStatus availabilityStatus;
//        @NotBlank(message = "price is required")
//        @Size(min = 1, message = "price must have at list 1 digits")
        private Long price;
        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "technical_id", referencedColumnName = "technicalId")
        private TechnicalData technicalData;
        @OneToMany(mappedBy = "product")
        private Set<OrderEntity> orders = new HashSet<>();
        public enum AvailabilityStatus{
            AVAILABLE,
            NOT_AVAILABLE,
            COMING_SOON
        }
    }
