package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
public class PaymentMethodEntity{
    @Id
    private String code;
    private String name;

    @ManyToMany(mappedBy = "paymentMethods")
    private Set<RoomEntity> rooms = new LinkedHashSet<RoomEntity>();
}
