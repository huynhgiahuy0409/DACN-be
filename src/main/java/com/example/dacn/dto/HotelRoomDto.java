package com.example.dacn.dto;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.RoomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelRoomDto {
    private HotelEntity hotel;
    private RoomEntity room;
}
