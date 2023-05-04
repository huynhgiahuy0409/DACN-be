package com.example.dacn.services;

import com.example.dacn.model.HotelEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


public interface HotelService {
    HotelEntity findById(Long id) throws Exception;

    HotelEntity getHotel(Long id) throws Exception;

    List<HotelEntity> getAllHotel() throws Exception;

    HotelEntity createHotel(HotelEntity hotel) throws Exception;

    ResponseEntity<HotelEntity> updateHotel(Long id, HotelEntity hotel) throws Exception;

    ResponseEntity<Map<String, Boolean>> deleteHotel(Long id) throws Exception;
}
