package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "hotel_image")
@Getter
@Setter
public class HotelImageEntity  extends ImageBase{

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;
}
