package com.dealership.car.model;

import com.dealership.car.dynamic.IdentifiableEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Entity
@Getter
@Setter
@Table(name = "`keys`")
public class Keys extends BaseEntity implements IdentifiableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY  )
    private Integer Id;
    @NotBlank(message = "password is required")
    @Size(min = 3, message = "password must be at list 3 char")
    private String password;
    @OneToOne(mappedBy = "keys", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JsonIgnore
    private Person person;

    @Override
    public Integer getId() {
        return Id;
    }
}
