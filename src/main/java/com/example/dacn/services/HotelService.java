package com.example.dacn.services;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.model.ValidSearchedProduct;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import com.example.dacn.responsemodel.SearchedProductItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import com.example.dacn.responsemodel.AverageRatingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface HotelService {
    HotelEntity findById(Long id) throws Exception;

    HotelEntity getHotel(Long id) throws Exception;

    HotelEntity getOne(Long id);

    HotelEntity getOne(Specification<HotelEntity> specification);

    List<HotelEntity> getAllHotel() throws Exception;

    HotelEntity createHotel(HotelEntity hotel) throws Exception;

    ResponseEntity<HotelEntity> updateHotel(Long id, HotelEntity hotel) throws Exception;

    ResponseEntity<Map<String, Boolean>> deleteHotel(Long id) throws Exception;

    List<HotelEntity> findAll(Specification specification, Pageable pageable);

    List<HotelEntity> findAll(Specification specification);

    List<HotelEntity> findAll(Specification specification, Sort sort);

    Double computeStarRating(Double hotelPoints);

    AverageRatingResponse getAverageRatingResponse(HotelEntity hotel);

    ValidSearchedProduct findSearchedProductItem(ProductFilterRequest productFilterRequest);

    List<Object> findRelativeProductItem(ProductFilterRequest productFilterRequest);
}
