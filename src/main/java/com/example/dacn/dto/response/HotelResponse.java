package com.example.dacn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class HotelResponse {
    private Long id;
    private String name;
    private String description;
    private Double averagePoints;
    private String status;
}
