package com.example.dacn.dto.response;

import com.example.dacn.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@Builder
public class ReservationResponse {
    private Long id;
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
    private RoomResponse room;
    private HotelResponse hotel;
}
