package com.dealership.car.DTO;

import com.dealership.car.model.Keys;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonDto {
    @Size(min = 2, message = "Username must be at list 2 char")
    @NotBlank(message = "username required")
    private String username;
    @Transient
    //TODO solve problem with validation?because i need this
//    @NotBlank(message = "password required")
//    @Size(min = 3, message = "password must be at list 3 char")
    private String password;
    @Size(min = 2, message = "first name must be at list 2 char")
    @NotBlank(message = "first name required")
    private String firstName;
    @Size(min = 2, message = "last name must be at list 2 char")
    @NotBlank(message = "last name required")
    private String lastName;
    @NotBlank(message = "passport series required")
    @Size(min = 5, message = "passport series must be at list 5 char")
    private String passportSeries;
    @Size(min = 10, message = "password number must be at list 10 char")
    @NotBlank(message = "passport number required")
    private String passportNumber;
    @Size(min = 5, message = "address must be at list 5 char")
    @NotBlank(message = "address is required")
    private String address;
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    @NotBlank(message = "mobile number is required")
    private String mobileNumber;
}
