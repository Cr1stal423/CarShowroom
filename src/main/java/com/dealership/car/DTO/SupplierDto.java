package com.dealership.car.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierDto {

    private Integer orderId;
    @NotBlank(message = "Name is required")
    private String name;

    private Boolean isDelayed;
}
