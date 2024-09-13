package com.dealership.car.model;

import com.dealership.car.dynamic.IdentifiableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@RequiredArgsConstructor

public class OrderEntity extends BaseEntity implements IdentifiableEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer orderId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @ToString.Exclude
    private Supplier supplier;
    //TODO: need to remove because i have created at
    private LocalDateTime orderTime;
    @NotNull(message = "delivery is required")
    private boolean delivery;
    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "payment type is required")
    private PaymentType paymentType;
//    @NotBlank(message = "payment method is required")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column(nullable = true)
    private boolean flag;

    @Override
    public Integer getId() {
        return orderId;
    }

    public enum PaymentMethod {
        CARD,
        CASH
    }
    public enum PaymentType{
        CREDIT,
        BUY
    }
    public boolean isFlag() {
        return flag;
    }
}
