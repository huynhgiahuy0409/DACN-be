package com.example.dacn.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "favorite_hotel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FavoriteHotelEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private HotelEntity hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
