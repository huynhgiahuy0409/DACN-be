package com.example.dacn.requestmodel;

import lombok.Data;

import java.util.List;

@Data
public class OptionFilterRequest {
    private List<Long> hotelFacilities;
    private List<Long> benefits;
    private Double guestRating;
    private Double discount;
    private Double priceFrom;
    private Double priceTo;
}
