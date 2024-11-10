package com.dealership.car.model;

import com.dealership.car.dynamic.IdentifiableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.naming.ldap.PagedResultsControl;

@Entity
@Data
public class Roles extends BaseEntity implements IdentifiableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;
    @NotBlank(message = "role name is required")
    private String roleName;

    @Override
    public Integer getId() {
        return roleId;
    }
}
