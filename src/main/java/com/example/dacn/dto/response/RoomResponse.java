package com.example.dacn.dto.response;

import lombok.Data;

@Data
public class RoomResponse {
    private Long id;
    private String name;
    private Integer maxAdults;
    private Integer maxChildren;
    private String status;
    private Double originPrice;
    private Double rentalPrice;
    private Double finalPrice;
}
