package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
public class AddressEntity extends BaseEntity{
    private String address;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private ProvinceEntity province;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private WardEntity ward;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private DistrictEntity district;

    @OneToOne(mappedBy = "address")
    private HotelEntity hotel;


}
