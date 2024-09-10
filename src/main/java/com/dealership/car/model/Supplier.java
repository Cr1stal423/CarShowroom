package com.dealership.car.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Supplier extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "supplier", cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OrderEntity> orders = new HashSet<>();

    private Boolean isDelayed;

    public void addOrder(OrderEntity orderEntity){
        if (orders == null){
            orders = new HashSet<>();
        }
        orders.add(orderEntity);
    }
}
