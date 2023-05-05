package com.example.dacn.services;

import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.RoomEntity;
import com.example.dacn.responsemodel.AverageRatingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface HotelService {
    HotelEntity findById(Long id) throws Exception;
    List<HotelEntity> findAll(Specification specification, Pageable pageable);
    List<HotelEntity> findAll(Specification specification);
    List<HotelEntity> findAll(Specification specification, Sort sort);
    HotelEntity findOne(Specification<HotelEntity> spec);
    HotelEntity findOne(Long id);
    Double computeStarRating(Double hotelPoints);

    AverageRatingResponse getAverageRatingResponse(HotelEntity hotel);

}
