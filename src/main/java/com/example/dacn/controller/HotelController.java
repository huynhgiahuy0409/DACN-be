package com.example.dacn.controller;

import com.example.dacn.model.HotelEntity;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.impl.HotelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.dacn.dto.response.AutocompleteSearchResponse;
import com.example.dacn.model.*;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.requestmodel.ProductSortRequest;
import com.example.dacn.responsemodel.APIResponse;
import com.example.dacn.responsemodel.AddressResponse;
import com.example.dacn.responsemodel.DiscountResponse;
import com.example.dacn.responsemodel.SearchedProductItemResponse;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.IBenefitService;
import com.example.dacn.services.IProvinceService;
import com.example.dacn.services.RoomService;
import com.example.dacn.specification.BenefitSpecification;
import com.example.dacn.specification.HotelSpecification;
import com.example.dacn.specification.RoomSpecification;
import com.example.dacn.specification.builder.HotelSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin("http://localhost:4200")
public class HotelController {


    @Autowired
    private HotelService hotelService;
    @Autowired
    private IBenefitService benefitService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private IProvinceService provinceService;

    @GetMapping("/hotel_profiles")
    public List<HotelEntity> getAllHotelEntitys() throws Exception {
       return hotelService.getAllHotel();

    }


    @PostMapping("/hotel_profile")
    public HotelEntity createHotelEntity(@RequestBody HotelEntity hp) throws Exception {
      return hotelService.createHotel(hp);
    }

    @GetMapping("/hotel_profiles/{id}")
    public HotelEntity getHotelEntity(@PathVariable Long id) throws Exception {
       return hotelService.getHotel(id);
    }

    @PutMapping("/hotel_profiles/{id}")
    public ResponseEntity<HotelEntity> updateHotelEntity(@PathVariable Long id, @RequestBody HotelEntity hotelEntity) throws Exception {
      return hotelService.updateHotel(id,hotelEntity);
      
    }

    @DeleteMapping("/hotel_profiles/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteHotelEntity(@PathVariable Long id) throws Exception {
        return hotelService.deleteHotel(id);
    }
    @GetMapping("/autocomplete-search/{searchValue}")
    public ResponseEntity autocompleteSearchedHotel(@PathVariable String searchValue) {
        List<AutocompleteSearchResponse> autocompleteHotels = new LinkedList<>();
        APIResponse<List<AutocompleteSearchResponse>> response = new APIResponse<>(autocompleteHotels, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        HotelSpecificationBuilder hotelBuilder = new HotelSpecificationBuilder();
        HotelSpecificationBuilder locationBuilder = new HotelSpecificationBuilder();
        hotelBuilder.with("name", searchValue, "like");
        locationBuilder.with("_domain", searchValue, "province-like");
        Specification<HotelEntity> specification = hotelBuilder.build();
        Specification<HotelEntity> localtionSpecification = locationBuilder.build();
        Pageable pageable = PageRequest.of(0, 3);
        List<HotelEntity> foundHotels = this.hotelService.findAll(specification, pageable);
        foundHotels.forEach(hotel -> {
            AutocompleteSearchResponse autocompleteHotel = new AutocompleteSearchResponse();
            autocompleteHotel.setCategory("Khách sạn");
            AddressEntity address = hotel.getAddress();
            ProvinceEntity province = address.getProvince();
            DistrictEntity district = address.getDistrict();
            WardEntity ward = address.getWard();
            StringBuilder sb = new StringBuilder();
            sb.append(hotel.getName() + ", ").append(ward.get_prefix() + " " + ward.get_name() + ", ").append(district.get_prefix() + " " + district.get_name() + ", ").append(province.get_domain());
            autocompleteHotel.setName(sb.toString());
            autocompleteHotel.setType("property");
            autocompleteHotel.setHotelId(hotel.getId());
            autocompleteHotel.setSearchValue(hotel.getName());
            autocompleteHotels.add(autocompleteHotel);
        });
        List<HotelEntity> foundLocationHotels = this.hotelService.findAll(localtionSpecification, pageable);
        foundLocationHotels.forEach(locationHotel -> {
            AutocompleteSearchResponse autocompleteHotel = new AutocompleteSearchResponse();
            autocompleteHotel.setCategory("Thành phố");
            AddressEntity address = locationHotel.getAddress();
            ProvinceEntity province = address.getProvince();
            DistrictEntity district = address.getDistrict();
            StringBuilder sb = new StringBuilder();
            sb.append(district.get_prefix() + " " + district.get_name() + ", ").append(province.get_domain());
            autocompleteHotel.setName(sb.toString());
            autocompleteHotel.setType("city");
            autocompleteHotel.setSearchValue(province.get_domain());
            autocompleteHotels.add(autocompleteHotel);
        });
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity searchHotel(@RequestBody(required = true) ProductFilterRequest productFilterRequest) {
        List<SearchedProductItemResponse> data = new LinkedList<>();
        Set<HotelEntity> foundHotels = new LinkedHashSet<>();
        ProvinceEntity searchedHotelProvince;
        if (productFilterRequest.getHotelId() != null) {
            HotelEntity searchedHotel = this.hotelService.findOne(productFilterRequest.getHotelId());
            searchedHotelProvince = searchedHotel.getAddress().getProvince();
            foundHotels.add(searchedHotel);
        } else {
            searchedHotelProvince = provinceService.findBy_domain(productFilterRequest.getSearch());
        }
        HotelSpecificationBuilder relativeHotelBuilder = new HotelSpecificationBuilder();
        Specification<HotelEntity> relativeHotelSpec = relativeHotelBuilder.with("_domain", searchedHotelProvince.get_domain(), "province-like").with("maxAdults", productFilterRequest.getAdults(), "valid room capacity").with("maxChildren", productFilterRequest.getChildren(), "valid room capacity").build();
        if(productFilterRequest.getProductSortRequest() != null){
            relativeHotelSpec = relativeHotelBuilder.with("_domain", searchedHotelProvince.get_domain(), "province-like").with("maxAdults", productFilterRequest.getAdults(), "valid room capacity").with("maxChildren", productFilterRequest.getChildren(), "valid room capacity").build().and(HotelSpecification.sortByAscProperty(productFilterRequest.getProductSortRequest().getProperty()));
        }
        List<HotelEntity> sameProvinceHotels =  this.hotelService.findAll(relativeHotelSpec);
        for (HotelEntity sameProvinceHotel : sameProvinceHotels) {
            if (!foundHotels.contains(sameProvinceHotel)) {
                foundHotels.add(sameProvinceHotel);
            }
        }
        for (HotelEntity foundHotel : foundHotels) {
            SearchedProductItemResponse searchedProductItem = new SearchedProductItemResponse();
            if (foundHotel.getName().equals(productFilterRequest.getSearch()) && productFilterRequest.getHotelId() != null) {
                searchedProductItem.setIsSearchedHotel(true);
            }
            searchedProductItem.setName(foundHotel.getName());
            Specification<BenefitEntity> distinctBenefitSpec = BenefitSpecification.distinctBenefitsByHotel(foundHotel.getId());
            List<BenefitEntity> distinctBenefits = this.benefitService.findAll(distinctBenefitSpec);
            searchedProductItem.setBenefits(distinctBenefits.stream().map(b -> b.getName()).collect(Collectors.toList()));
            AddressEntity foundHotelAddress = foundHotel.getAddress();
            AddressResponse addressResponse = new AddressResponse(foundHotelAddress.getId(), foundHotelAddress.getStreet(), foundHotelAddress.getProvince().get_domain(), foundHotelAddress.getDistrict().get_prefix() + " " + foundHotelAddress.getDistrict().get_name(), foundHotelAddress.getWard().get_prefix() + " " + foundHotelAddress.getWard().get_name());
            searchedProductItem.setAddress(addressResponse);
            searchedProductItem.setStartRating(this.hotelService.computeStarRating(foundHotel.getAveragePoints()));
            Specification<RoomEntity> searchedRoom = RoomSpecification.findCheapestRoomWithValidCapacity(foundHotel.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
            RoomEntity cheapestRoom = this.roomService.findOne(searchedRoom);
            searchedProductItem.setOriginalPrice(cheapestRoom.getOriginPrice());
            searchedProductItem.setRentalPrice(cheapestRoom.getRentalPrice());
            searchedProductItem.setAverageRating(this.hotelService.getAverageRatingResponse(foundHotel));
            searchedProductItem.setIsDeals(foundHotel.getIsDeals());
            DiscountEntity roomDiscount = cheapestRoom.getDiscount();
            DiscountResponse discountResponse = new DiscountResponse();
            discountResponse.setName(roomDiscount.getName());
            discountResponse.setPercent(roomDiscount.getDiscountPercent());
            searchedProductItem.setDiscount(discountResponse);
            searchedProductItem.setFinalPrice(cheapestRoom.getFinalPrice());
            searchedProductItem.setIsOnlinePayment(cheapestRoom.getPaymentMethods().size() > 0 ? true : false);
            searchedProductItem.setIsFreeCancellation(foundHotel.getIsFreeCancellation());
            data.add(searchedProductItem);
        }
        APIResponse<List<SearchedProductItemResponse>> response = new APIResponse<>(data, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}
