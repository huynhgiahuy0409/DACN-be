package com.example.dacn.services;

import com.example.dacn.entity.HotelImageEntity;

public interface IHotelImageService {
    String findFirstBannerImage(Long hotelId);
}
