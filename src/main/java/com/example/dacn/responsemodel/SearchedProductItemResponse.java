package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.List;

@Data
public class SearchedProductItemResponse {
    private String name;
    private List<String> benefits;
    private AddressResponse address;
    private Double startRating;
    private Double originalPrice;
    private Double rentalPrice;
    private Double finalPrice;
    private AverageRatingResponse averageRating;
    private DiscountResponse discount;
    private Boolean isSearchedHotel;
    private Boolean isDeals;
    private Boolean isOnlinePayment;
    private Boolean isFreeCancellation;
}
