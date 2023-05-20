package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.List;

@Data
public class SearchedProductItemResponse {
    private String name;
    private List<String> benefits;
    private AddressResponse address;
    private Double starRating;
    private Double originalPrice;
    private Double rentalPrice;
    private Double finalPrice;
    private AverageRatingResponse averageRating;
    private DiscountResponse discount;
    private Boolean isDeals;
    private Boolean isOnlinePayment;
    private Boolean isFreeCancellation;

    public SearchedProductItemResponse() {
    }

    public SearchedProductItemResponse(String name, List<String> benefits, AddressResponse address, Double startRating, Double originalPrice, Double rentalPrice, Double finalPrice, AverageRatingResponse averageRating, DiscountResponse discount, Boolean isDeals, Boolean isOnlinePayment, Boolean isFreeCancellation) {
        this.name = name;
        this.benefits = benefits;
        this.address = address;
        this.starRating = startRating;
        this.originalPrice = originalPrice;
        this.rentalPrice = rentalPrice;
        this.finalPrice = finalPrice;
        this.averageRating = averageRating;
        this.discount = discount;
        this.isDeals = isDeals;
        this.isOnlinePayment = isOnlinePayment;
        this.isFreeCancellation = isFreeCancellation;
    }

    public SearchedProductItemResponse(String name, Double originalPrice, Double rentalPrice, Double finalPrice, Boolean isDeals, Boolean isFreeCancellation) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.rentalPrice = rentalPrice;
        this.finalPrice = finalPrice;
        this.isDeals = isDeals;
        this.isFreeCancellation = isFreeCancellation;
    }
}
