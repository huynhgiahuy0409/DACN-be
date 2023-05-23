package com.example.dacn.services.impl;

import com.example.dacn.entity.*;
import com.example.dacn.model.SearchedProductSorter;
import com.example.dacn.model.ValidSearchedProduct;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.repository.RoomRepository;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.requestmodel.ProductSortRequest;
import com.example.dacn.responsemodel.*;
import com.example.dacn.services.*;
import com.example.dacn.specification.BenefitSpecification;
import com.example.dacn.specification.HotelSpecification;
import com.example.dacn.specification.RoomSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private IBenefitService benefitService;

    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public SearchedProductResponse getSearchedProductFromAutocomplete(ProductFilterRequest productFilterRequest) {
        SearchedProductResponse result = null;
        if (productFilterRequest.getType().equals("hotel")) {
            result = new SearchedProductResponse();
            ValidSearchedProduct validFindHotel = this.hotelService.findSearchedProductItem(productFilterRequest);
            SearchedProductItemResponse thisValidProduct = validFindHotel != null? this.buildSearchProductItem(validFindHotel): null;
            result.setSearchedProduct(thisValidProduct);
            List<Object> relativeValidProducts =
                    this.hotelService.findRelativeProductItem(productFilterRequest);
            List<SearchedProductItemResponse> relativeValidProductsResponse = new LinkedList<>();
            for (Object hotelFacilityOption : relativeValidProducts) {
                Object[] responses = Arrays.stream((Object[])hotelFacilityOption).map(o -> {
                    return o.toString();
                }).toArray(Object[]::new);
                if(Long.parseLong(responses[0].toString()) != productFilterRequest.getValue()){
                    ValidSearchedProduct validSearchedProduct = new ValidSearchedProduct(Long.parseLong(responses[0].toString()), Long.parseLong(responses[1].toString()));
                    relativeValidProductsResponse.add(this.buildSearchProductItem(validSearchedProduct));
                }
            }
            result.setRelativeSearchedProducts(relativeValidProductsResponse);
            Double minPrice = this.roomService.minPriceByFilter(productFilterRequest);
            Double maxPrice = this.roomService.maxPriceByFilter(productFilterRequest);
            result.setMinFinalPrice(minPrice);
            result.setMaxFinalPrice(maxPrice);
        }
        return result;
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
    private SearchedProductItemResponse buildSearchProductItem(ValidSearchedProduct validSearchedProduct){
        HotelEntity foundHotel = this.hotelService.getOne(validSearchedProduct.getHotelId());
        RoomEntity foundRoom = this.roomService.getOne(validSearchedProduct.getRoomId());
        Specification<BenefitEntity> benefitSpec = BenefitSpecification.distinctBenefitsByHotel(foundHotel.getId());
        List<String> benefits = this.benefitService.findAll(benefitSpec).stream().map(benefitEntity -> benefitEntity.getName()).collect(Collectors.toList());
        AddressResponse addressResponse = this.getAddressResponse(foundHotel);
        Double starRating = this.hotelService.computeStarRating(foundHotel.getAveragePoints());
        AverageRatingResponse averageRatingResponse = this.hotelService.getAverageRatingResponse(foundHotel);
        DiscountResponse discountResponse = this.getDiscountResponse(foundRoom);
        SearchedProductItemResponse result = new SearchedProductItemResponse(validSearchedProduct.getHotelId(), foundHotel.getName(), benefits, addressResponse, starRating,  foundRoom.getOriginPrice(), foundRoom.getRentalPrice(), foundRoom.getFinalPrice(),averageRatingResponse, discountResponse, foundHotel.getIsDeals(), foundRoom.getPaymentMethods().size() > 0, foundHotel.getIsFreeCancellation());
        return result;
    }

}
