package com.example.dacn.responsemodel;

import com.example.dacn.dto.response.BenefitResponse;
import com.example.dacn.entity.BenefitEntity;
import com.example.dacn.entity.DiscountEntity;
import com.example.dacn.entity.RoomImageEntity;
import lombok.Data;

import java.util.Set;

@Data
public class RoomResponse {
    private Long id;
    private String name;
    private Integer maxAdults;
    private Integer maxChildren;
    private String status;
    private Double originPrice;
    private Double rentalPrice;
    private Double finalPrice;
    private Set<BenefitResponse> benefits;
    private DiscountResponse discount;
    private Set<ImageResponse> roomImages;
}
