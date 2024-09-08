package com.dealership.car.DTO;

import com.dealership.car.model.OrderEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for handling order details.
 *
 * This class is used to transfer order-related data between different layers of the application.
 * It includes fields for product ID, person ID, delivery option, payment type, and payment method.
 *
 * The class ensures that mandatory fields such as product ID, person ID, and delivery option are not null through
 * validation annotations.
 */
@Data
public class OrderDto {
    @NotNull(message = "Product is required")
    private Integer productId;
    @NotNull(message = "Person is required")
    private Integer personId;
    @NotNull(message = "delivery option is required")
    private Boolean delivery;

    private OrderEntity.PaymentType paymentType;

    private OrderEntity.PaymentMethod paymentMethod;
}
