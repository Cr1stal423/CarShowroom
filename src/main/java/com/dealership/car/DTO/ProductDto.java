package com.dealership.car.DTO;

import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Digits(integer = 12, fraction = 0, message = "The number must be between 3 and 12 digits.")
    @Min(value = 10, message = "The number must be at least 3 digits long.")
    @Max(value = 999999999999L, message = "The number must be at most 12 digits long.")
    private Long price;

    private TechnicalData.BodyType bodyType;

    @NotNull(message = "doors is required")
    @Digits(integer = 2, fraction = 0, message = "Doors must be a number with at most 2 digits")
    private Integer doors;


    @NotNull(message = "seats is required")
    @Digits(integer = 1, fraction = 0, message = "Seats must be a number with at most 1 digit")
    private Integer seats;


    private TechnicalData.EngineType engineType;


    private TechnicalData.EnginePlacement enginePlacement;


    @NotNull(message = "engine capacity is required")
    @Digits(integer = 2, fraction = 2, message = "Engine capacity must be a number with at most 2 digits")

    private Double engineCapacity;
}
