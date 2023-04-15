package com.example.dacn.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class CartRequest {
    private Integer adult;
    private Integer child;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate toDate;
    private Long hotelId;
    private Long roomId;
    private String sessionId;
}
