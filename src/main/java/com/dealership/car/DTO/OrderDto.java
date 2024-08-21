package com.dealership.car.DTO;

import com.dealership.car.model.OrderEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
