package com.example.dacn.services.impl;

import com.example.dacn.entity.*;
import com.example.dacn.model.SearchedProductSorter;
import com.example.dacn.responsemodel.*;
import com.example.dacn.services.*;
import com.example.dacn.specification.BenefitSpecification;
import com.example.dacn.specification.HotelSpecification;
import com.example.dacn.specification.RoomSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchedProductService implements ISearchedProductService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private IBenefitService benefitService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RoomService roomService;

    @Override
    public SearchedProductResponse getSearchedProductFromAutocomplete(Long id, String type, Integer maxAdult, Integer maxChildren, SearchedProductSorter sorter) {
        SearchedProductResponse result = new SearchedProductResponse();
        if (type.equals("hotel")) {
            HotelEntity defaultHotel = this.hotelService.getOne(id);
            Specification<HotelEntity> validSearchHotelSpec = HotelSpecification.findValidSearchedHotel(id, maxAdult, maxChildren);
            HotelEntity searchedValidHotel = this.hotelService.findAll(validSearchHotelSpec).size() > 0 ? this.hotelService.getOne(validSearchHotelSpec) : null;
            SearchedProductItemResponse searchedProductItem = searchedValidHotel != null ? this.createOne(searchedValidHotel, maxAdult, maxChildren) : null;

            List<Long> hotelIds = this.findHotelsByProvince(defaultHotel.getAddress().getProvince().getId()).stream().map(hotel -> hotel.getId()).collect(Collectors.toList());
            List<HotelEntity> relativeValidHotels = this.hotelService.findAll(HotelSpecification.findValidSearchedHotel(hotelIds, maxAdult, maxChildren));

            List<SearchedProductItemResponse> searchedProductItems = relativeValidHotels.stream().map(hotel -> this.createOne(hotel, maxAdult, maxChildren)).collect(Collectors.toList());
            result.setSearchedProduct(searchedProductItem);
            result.setRelativeSearchedProducts(searchedProductItems);
        }
        if (type.equals("province")) {
            List<Long> hotelIds = this.findHotelsByProvince(id).stream().map(hotel -> hotel.getId()).collect(Collectors.toList());
            List<HotelEntity> relativeValidHotels = this.hotelService.findAll(HotelSpecification.findValidSearchedHotel(hotelIds, maxAdult, maxChildren));

            List<SearchedProductItemResponse> searchedProductItems = relativeValidHotels.stream().map(hotel -> this.createOne(hotel, maxAdult, maxChildren)).collect(Collectors.toList());
            result.setSearchedProduct(null);
            result.setRelativeSearchedProducts(searchedProductItems);
        }
        if (sorter != null) {
            if (sorter.getProperty().equals("price") && sorter.getDirection().equals("asc")) {
                if (sorter.getDirection().equals("asc")) {
                    Collections.sort(result.getRelativeSearchedProducts(), Comparator.comparing(SearchedProductItemResponse::getFinalPrice));
                } else {
                    Collections.sort(result.getRelativeSearchedProducts(), Comparator.comparing(SearchedProductItemResponse::getFinalPrice).reversed());
                }
            } else if (sorter.getProperty().equals("name")) {
                if (sorter.getDirection().equals("asc")) {
                    Collections.sort(result.getRelativeSearchedProducts(), Comparator.comparing(SearchedProductItemResponse::getName));
                } else {
                    Collections.sort(result.getRelativeSearchedProducts(), Comparator.comparing(SearchedProductItemResponse::getName).reversed());
                }
            } else if (sorter.getProperty().equals("rate")) {
                if (sorter.getDirection().equals("asc")) {
                    Collections.sort(result.getRelativeSearchedProducts(), (o1, o2) -> o1.getAverageRating().getPoints().compareTo(o2.getAverageRating().getPoints()));
                    Collections.reverse(result.getRelativeSearchedProducts());
                } else {
                    Collections.sort(result.getRelativeSearchedProducts(), (o1, o2) -> o1.getAverageRating().getPoints().compareTo(o2.getAverageRating().getPoints()));
                    Collections.reverse(result.getRelativeSearchedProducts());
                }
            } else {
            }
        }
        return result;
    }

    private List<HotelEntity> findHotelsByProvince(Long provinceId) {
        return this.hotelService.findAll(HotelSpecification.findByProvince(provinceId));
    }

    private SearchedProductItemResponse createOne(HotelEntity hotel, Integer maxAdults, Integer maxChildren) {
        SearchedProductItemResponse spir = new SearchedProductItemResponse();
        spir.setName(hotel.getName());
        Specification<BenefitEntity> benefitSpec = BenefitSpecification.distinctBenefitsByHotel(hotel.getId());
        List<String> benefits = this.benefitService.findAll(benefitSpec).stream().map(benefitEntity -> benefitEntity.getName()).collect(Collectors.toList());
        spir.setBenefits(benefits);
        spir.setAddress(this.getAddressResponse(hotel));
        spir.setStarRating(this.hotelService.computeStarRating(hotel.getAveragePoints()));
        RoomEntity validCheapestRoom = this.roomService.findOne(RoomSpecification.findCheapestRoomWithValidCapacity(hotel.getId(), maxAdults, maxChildren));
        spir.setOriginalPrice(validCheapestRoom.getOriginPrice());
        spir.setRentalPrice(validCheapestRoom.getRentalPrice());
        spir.setFinalPrice(validCheapestRoom.getFinalPrice());
        spir.setAverageRating(this.hotelService.getAverageRatingResponse(hotel));
        spir.setDiscount(this.getDiscountResponse(validCheapestRoom));
        spir.setIsDeals(hotel.getIsDeals());
        spir.setIsOnlinePayment(validCheapestRoom.getPaymentMethods().size() > 0);
        spir.setIsFreeCancellation(hotel.getIsFreeCancellation());
        return spir;
    }

    private AddressResponse getAddressResponse(HotelEntity hotel) {
        AddressEntity add = hotel.getAddress();
        return new AddressResponse(add.getId(), add.getStreet(), add.getProvince().get_domain(), add.getDistrict().get_name(), add.getWard().get_name());
    }

    private DiscountResponse getDiscountResponse(RoomEntity room) {
        if (room.getDiscount() != null) {
            DiscountEntity discountEntity = room.getDiscount();
            DiscountResponse discountResponse = new DiscountResponse();
            discountResponse.setName(discountEntity.getName());
            discountResponse.setPercent(discountEntity.getDiscountPercent());
            return discountResponse;
        }
        return null;
    }
}
