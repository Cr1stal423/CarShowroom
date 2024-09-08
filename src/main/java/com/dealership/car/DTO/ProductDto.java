package com.dealership.car.DTO;

import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for transferring Product information.
 * Provides essential details about a product including its origin,
 * brand, model, color, availability status, price, and technical specifications.
 */
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

    private Product.AvailabilityStatus availabilityStatus;

    @NotNull(message = "price is required")
    private Long price;

    private TechnicalData.BodyType bodyType;

    @NotNull(message = "doors is required")
    private Integer doors;

    @NotNull(message = "seats si required")
    private Integer seats;


    private TechnicalData.EngineType engineType;


    private TechnicalData.EnginePlacement enginePlacement;

//    @NotBlank(message = "engine capacity is required")
    @NotNull(message = "engine capacity is required")
    private Double engineCapacity;
}
