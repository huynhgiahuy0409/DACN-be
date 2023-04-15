package com.example.dacn.services;

import com.example.dacn.model.HotelEntity;


public interface HotelService {
    HotelEntity findById(Long id) throws Exception;
}
