package com.example.dacn.services;

import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.RoomEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RoomService {
    RoomEntity findByHotelAndRoomId(Long hotelId, Long roomId);

    RoomResponse setRoomStatus(Long hotelId, Long roomId, String status) throws Exception;


    RoomEntity  getRoom(Long id) throws Exception;

    List<RoomEntity > getAllRoom () throws Exception;

    RoomEntity  createRoom (RoomEntity  room) throws Exception;

    ResponseEntity<RoomEntity > updateRoom (Long id, RoomEntity  hotel) throws Exception;

    ResponseEntity<Map<String, Boolean>> deleteRoom (Long id) throws Exception;
}
