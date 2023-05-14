package com.example.dacn.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteHotelResponse {
    private Long id;
    private String bannerUrl;
    private String name;
    private String address;
    private Double avgRating;
    private Integer totalRating;
    private Double originPrice;
    private Double finalPrice;
}
