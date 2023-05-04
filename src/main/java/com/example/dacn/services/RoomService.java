package com.example.dacn.services;

import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.model.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

public interface RoomService {
    RoomEntity findByHotelAndRoomId(Long hotelId, Long roomId);

    RoomResponse setRoomStatus(Long hotelId, Long roomId, String status) throws Exception;

    RoomEntity findOne(Specification<RoomEntity> spec);

    Double computeFinalPrice(RoomEntity room);


}
