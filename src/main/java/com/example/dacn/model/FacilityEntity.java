package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "facility")
@Getter
@Setter
public class FacilityEntity extends BaseEntity{
    private String name;
    private String code;
    private String icon;

    @ManyToOne
    @JoinColumn(name = "hotel_facility_group_code")
    private HotelFacilityGroupEntity hotelFacilityGroup;

    @ManyToOne
    @JoinColumn(name = "room_facility_group_code")
    private RoomFacilityGroupEntity roomFacilityGroup;

    @ManyToMany(mappedBy = "facilities")
    private Set<RoomEntity> rooms = new LinkedHashSet<RoomEntity>();

    @ManyToMany(mappedBy = "facilities")
    private Set<HotelEntity> hotels = new LinkedHashSet<HotelEntity>();
}
