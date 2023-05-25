package com.example.dacn.model;

import com.example.dacn.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationEntity extends BaseEntity {
    private Double price;
    private Integer adult;
    private Integer children;


    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    private Double discountPercent;
    @Enumerated(EnumType.ORDINAL)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;
}
