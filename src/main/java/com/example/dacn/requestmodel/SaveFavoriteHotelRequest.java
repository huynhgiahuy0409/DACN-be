package com.example.dacn.requestmodel;

import lombok.Data;

@Data
public class SaveFavoriteHotelRequest {
    private Long hotelId;
    private String username;
}
