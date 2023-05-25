package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class SearchedProductItemResponse {
    private Long productId;
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
    private Collection<ImageResponse> hotelImages;

    public SearchedProductItemResponse() {
    }

    public SearchedProductItemResponse(Long productId, String name, List<String> benefits, AddressResponse address, Double starRating, Double originalPrice, Double rentalPrice, Double finalPrice, AverageRatingResponse averageRating, DiscountResponse discount, Boolean isDeals, Boolean isOnlinePayment, Boolean isFreeCancellation, Collection<ImageResponse> hotelImages) {
        this.productId = productId;
        this.name = name;
        this.benefits = benefits;
        this.address = address;
        this.starRating = starRating;
        this.originalPrice = originalPrice;
        this.rentalPrice = rentalPrice;
        this.finalPrice = finalPrice;
        this.averageRating = averageRating;
        this.discount = discount;
        this.isDeals = isDeals;
        this.isOnlinePayment = isOnlinePayment;
        this.isFreeCancellation = isFreeCancellation;
        this.hotelImages = hotelImages;
    }
}
