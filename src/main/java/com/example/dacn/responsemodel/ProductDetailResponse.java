package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetailResponse {
    private HotelResponse hotel;
    private List<RoomResponse> rooms;

    public ProductDetailResponse(HotelResponse hotel, List<RoomResponse> rooms) {
        this.hotel = hotel;
        this.rooms = rooms;
    }
}
