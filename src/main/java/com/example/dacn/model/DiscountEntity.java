package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "facility")
@Getter
@Setter
public class DiscountEntity extends BaseEntity{
    private String name;
    private String description;
    private Double discountPercent;
    private String status;
    private Date expiredDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
}
