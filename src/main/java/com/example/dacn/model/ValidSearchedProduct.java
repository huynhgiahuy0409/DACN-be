package com.example.dacn.model;

import lombok.Data;

@Data
public class ValidSearchedProduct {
    private Long hotelId;
    private Long roomId;

    public ValidSearchedProduct(Long hotelId, Long roomId) {
        this.hotelId = hotelId;
        this.roomId = roomId;
    }

    public ValidSearchedProduct() {
    }
}
