package com.dealership.car.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"orders", "keys"})
public class Person extends BaseEntity{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer personId;
    @OneToMany(mappedBy = "person")
    private Set<OrderEntity> orders = new HashSet<>();
    @Size(min = 2, message = "Username must be at list 2 char")
    @NotBlank(message = "username required")
    private String username;
    @Transient
    //TODO solve problem with validation?because i need this
//    @NotBlank(message = "password required")
//    @Size(min = 3, message = "password must be at list 3 char")
    private String password;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "keys_id", referencedColumnName = "id")
    @JsonIgnore // Ігнорує це поле при серіалізації в JSON
    private Keys keys;
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
    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH}, targetEntity = Roles.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
    @JsonIgnore // Ігнорує це поле при серіалізації в JSON
    private Roles roles;

}
