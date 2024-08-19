package com.dealership.car.DTO;

import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDto {
    @NotBlank(message = "origin country required")
    @Size(min = 2, message = "origin country must have at list 2 char")
    private String originCountry;

    @NotBlank(message = "brand is required")
    @Size(min = 3, message = "brand must have at list 3 char")
    private String brand;

    @NotBlank(message = "model is required")
    @Size(min = 2, message = "model must have at list 2 char")
    private String model;

    @NotBlank(message = "color is required")
    @Size(min = 2, message = "color must have at list 2 char")
    private String color;

    @NotBlank(message = "Availability Status is required")
    private Product.AvailabilityStatus availabilityStatus;

    @NotBlank(message = "price is required")
    @Size(min = 1, message = "price must have at list 1 digits")
    private Long price;

    @NotBlank(message = "body type is required")
    private TechnicalData.BodyType bodyType;

    @NotBlank(message = "doors is required")
    private Integer doors;

    @NotBlank(message = "seats is required")
    private Integer seats;

    @NotBlank(message = "engine type is required")
    private TechnicalData.EngineType engineType;

    @NotBlank(message = "engine placement is required")
    private TechnicalData.EnginePlacement enginePlacement;

    @NotBlank(message = "engine capacity is required")
    private Integer engineCapacity;
}
