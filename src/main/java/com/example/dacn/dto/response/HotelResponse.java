package com.example.dacn.dto.response;

import lombok.Data;

@Data
public class HotelResponse {
    private Long id;
    private String name;
    private String description;
    private Double averagePoints;
    private String status;
}
