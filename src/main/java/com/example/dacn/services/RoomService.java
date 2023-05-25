package com.example.dacn.services;

import com.example.dacn.dto.response.RoomResponse;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import com.example.dacn.entity.RoomEntity;
import com.example.dacn.requestmodel.ProductFilterRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;


public interface RoomService {
    Optional<RoomEntity> findByRoomId(Long roomId);

    RoomResponse setRoomStatus(Long hotelId, Long roomId, String status) throws Exception;


    RoomEntity getRoom(Long id) throws Exception;

    List<RoomEntity> getAllRoom() throws Exception;

    RoomEntity createRoom(RoomEntity room) throws Exception;

    ResponseEntity<RoomEntity> updateRoom(Long id, RoomEntity hotel) throws Exception;

    ResponseEntity<Map<String, Boolean>> deleteRoom(Long id) throws Exception;

    RoomEntity findOne(Specification<RoomEntity> spec);

    RoomEntity getOne(Long id);

    List<RoomEntity> findAll(Specification<RoomEntity> spec);

    Double computeFinalPrice(RoomEntity room);

    RoomEntity findMinimumPriceRoom(Long hotelId);

    Double minPriceByFilter(ProductFilterRequest productFilterRequest);

    Double maxPriceByFilter(ProductFilterRequest productFilterRequest);

    List<Long> findAllValidRoom(ProductFilterRequest productFilterRequest, Pageable pageable);

}
