package com.example.dacn.services;

import com.example.dacn.model.HotelEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import com.example.dacn.model.RoomEntity;
import com.example.dacn.responsemodel.AverageRatingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface HotelService {
    HotelEntity findById(Long id) throws Exception;

    HotelEntity getHotel(Long id) throws Exception;

    List<HotelEntity> getAllHotel() throws Exception;

    HotelEntity createHotel(HotelEntity hotel) throws Exception;

    ResponseEntity<HotelEntity> updateHotel(Long id, HotelEntity hotel) throws Exception;

    ResponseEntity<Map<String, Boolean>> deleteHotel(Long id) throws Exception;
    List<HotelEntity> findAll(Specification specification, Pageable pageable);
    List<HotelEntity> findAll(Specification specification);
    List<HotelEntity> findAll(Specification specification, Sort sort);
    HotelEntity findOne(Specification<HotelEntity> spec);
    HotelEntity findOne(Long id);
    Double computeStarRating(Double hotelPoints);

    AverageRatingResponse getAverageRatingResponse(HotelEntity hotel);

}
