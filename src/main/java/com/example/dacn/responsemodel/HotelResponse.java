package com.example.dacn.responsemodel;

import com.example.dacn.entity.AddressEntity;
import com.example.dacn.entity.FacilityEntity;
import lombok.Data;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class HotelResponse {
    private Long id;
    private String name;
    private String description;
    private Double averagePoints;
    private String status;
    private Boolean isFreeCancellation;
    private Boolean isDeals;
    private Set<ImageResponse> hotelImages;
    private AddressResponse address;
    private Double starRating;
    private AverageRatingResponse averageRating;
    private Set<FacilityResponse> facilities;
}
