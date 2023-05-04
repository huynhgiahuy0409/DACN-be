package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "discount")
@Getter
@Setter
public class DiscountEntity extends BaseEntity{
    private String name;
    private String description;
    private Double discountPercent;
    private String status;
    private Date expiredDate;

    @OneToMany(mappedBy = "discount")
    private List<RoomEntity> rooms;
}
