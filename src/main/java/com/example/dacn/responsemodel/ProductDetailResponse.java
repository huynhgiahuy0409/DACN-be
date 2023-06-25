package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductDetailResponse {
    private HotelResponse hotel;
    private List<RoomResponse> rooms;
    private Set<HotelFacilityGroupResponse> hotelFacilityGroups;

    public ProductDetailResponse(HotelResponse hotel, List<RoomResponse> rooms, Set<HotelFacilityGroupResponse> hotelFacilityGroupResponses) {
        this.hotel = hotel;
        this.rooms = rooms;
        this.hotelFacilityGroups = hotelFacilityGroupResponses;
    }
}
