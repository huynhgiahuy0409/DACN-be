package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "hotel")
@Getter
@Setter
public class HotelEntity extends BaseEntity {
    private String name;
    private String description;
    private Double averagePoints;
    private String status;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity owner;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "hotel")
    private Set<RoomEntity> rooms = new HashSet<RoomEntity>();

    @ManyToMany
    @JoinTable(name = "hotel_facility", joinColumns = @JoinColumn(name = "hotel_id"), inverseJoinColumns = @JoinColumn(name = "facility_id"))
    private Set<FacilityEntity> facilities = new LinkedHashSet<FacilityEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<HotelImageEntity> hotelImages = new HashSet<HotelImageEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<ReservationEntity> reservations = new LinkedHashSet<ReservationEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<CartEntity> cartItems = new LinkedHashSet<CartEntity>();
}
