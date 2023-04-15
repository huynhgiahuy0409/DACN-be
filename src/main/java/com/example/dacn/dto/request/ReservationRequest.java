package com.example.dacn.dto.request;

import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Getter
public class ReservationRequest {
    private Double price;
    private Integer adult;
    private Integer children;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    private Double discountPercent;
    private String username;
    private Long hotelId;
    private Long roomId;
}
