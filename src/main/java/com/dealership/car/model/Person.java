package com.dealership.car.model;

import com.dealership.car.dynamic.IdentifiableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = {"orders", "keys"})
@EqualsAndHashCode
public class Person extends BaseEntity implements IdentifiableEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer personId;

    @OneToMany(mappedBy = "person")
    private Set<OrderEntity> orders = new HashSet<>();

    private String username;
    @Transient
    //TODO solve problem with validation?because i need this
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "keys_id", referencedColumnName = "id")
    @JsonIgnore
    private Keys keys;

    private String firstName;

    private String lastName;

    private String passportSeries;

    private String passportNumber;

    private String address;

    private String mobileNumber;
    @OneToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "role_id", referencedColumnName = "roleId", nullable = false)
    @JsonIgnore
    private Roles roles;

    @Override
    public Integer getId() {
        return personId;
    }


}
