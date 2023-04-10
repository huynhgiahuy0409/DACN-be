package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class ReservationEntity extends BaseEntity {
    private Double price;
    private Integer adult;
    private Integer children;
    private Date startDate;
    private Date endDate;
    private String discountPercent;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "reservation_room", joinColumns = @JoinColumn(name = "reservation_id"), inverseJoinColumns = @JoinColumn(name = "room_id"))
    private Set<RoomEntity> rooms = new LinkedHashSet<>();
}
