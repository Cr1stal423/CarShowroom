package com.dealership.car.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Contact extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @NotBlank(message = "mobile number is required")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile must be 10 digits")
    private String mobileNumber;
    @NotBlank(message = "message is required")
    private String message;

    private String status;

}
