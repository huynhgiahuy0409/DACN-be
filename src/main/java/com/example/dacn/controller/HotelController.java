package com.example.dacn.controller;

import com.example.dacn.entity.*;
import com.example.dacn.model.SearchedProductSorter;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.repository.RoomRepository;
import com.example.dacn.requestmodel.OptionFilterRequest;
import com.example.dacn.requestmodel.ProductSortRequest;
import com.example.dacn.responsemodel.*;
import com.example.dacn.services.*;
import com.example.dacn.services.impl.HotelServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.dacn.dto.response.AutocompleteSearchResponse;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.services.HotelService;
import org.springframework.http.HttpStatus;

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
    @Autowired
    private IAutocompleteService autocompleteService;
    @Autowired
    private ISearchedProductService searchedProductService;
    @Autowired
    private ModelMapper mp;
    @Autowired
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
        return hotelService.updateHotel(id, hotelEntity);

    }

    @DeleteMapping("/hotel_profiles/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteHotelEntity(@PathVariable Long id) throws Exception {
        return hotelService.deleteHotel(id);

    }
    @GetMapping("/autocomplete-search/{searchValue}")
    public ResponseEntity autocompleteSearchedHotel(@PathVariable String searchValue) {
        List<AutocompleteSearchResponse> data = this.autocompleteService.buildAnAutocompletes(searchValue);
        APIResponse<List<AutocompleteSearchResponse>> response = new APIResponse<>(data, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity searchHotel(@RequestBody(required = true) ProductFilterRequest productFilterRequest) {
        SearchedProductResponse data = searchedProductService.getSearchedProductFromAutocomplete(productFilterRequest);
        APIResponse<SearchedProductResponse> response = new APIResponse<>(data, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{hotelId}")
    public ResponseEntity findDetailHotel(@RequestBody(required = true) ProductFilterRequest productFilterRequest){
        System.out.println(productFilterRequest.getValue());
        HotelEntity hotelEntity = this.hotelService.getOne(productFilterRequest.getValue());
        List<Long> validRoomIds = this.roomService.findAllValidRoom(productFilterRequest, PageRequest.of(0, 10));
        List<RoomEntity> roomEntities = validRoomIds.stream().map(id -> this.roomService.getOne(id)).collect(Collectors.toList());
        HotelResponse hotel = this.mp.map(hotelEntity, HotelResponse.class);
        AddressEntity a = hotelEntity.getAddress();
        AddressResponse addressResponse = new AddressResponse(a.getId(), a.getStreet(), a.getProvince().get_domain(), a.getDistrict().get_prefix() + " " + a.getDistrict().get_name(), a.getWard().get_prefix() + " " + a.getWard().get_name());
        Double starRating = this.hotelService.computeStarRating(hotelEntity.getAveragePoints());
        AverageRatingResponse averageRatingResponse = this.hotelService.getAverageRatingResponse(hotelEntity);
        hotel.setAddress(addressResponse);
        hotel.setStarRating(starRating);
        hotel.setAverageRating(averageRatingResponse);
        List<RoomResponse> rooms = roomEntities.stream().map(roomEntity -> {
            return mp.map(roomEntity, RoomResponse.class);
        }).collect(Collectors.toList());

        Set<FacilityEntity> facilities = hotelEntity.getFacilities();
        Set<HotelFacilityGroupResponse> hotelFacilityGroupResponses = new LinkedHashSet<>();

        Set<HotelFacilityGroupEntity> hotelFacilityGroupEntities = new LinkedHashSet<>();
        facilities.forEach(f -> {
            HotelFacilityGroupEntity hotelFacilityGroupEntity = f.getHotelFacilityGroup();
            if(!hotelFacilityGroupEntities.contains(hotelFacilityGroupEntity)){
                hotelFacilityGroupEntities.add(hotelFacilityGroupEntity);
            }
        });

        hotelFacilityGroupEntities.forEach(hfg -> {
            HotelFacilityGroupResponse hotelFacilityGroupResponse = new HotelFacilityGroupResponse();
            hotelFacilityGroupResponse.setId(hfg.getId());
            hotelFacilityGroupResponse.setName(hfg.getName());
            Set<FacilityResponse> facilityResponses = new LinkedHashSet<>();
            for (FacilityEntity facility : facilities) {
                HotelFacilityGroupEntity hotelFacilityGroupEntity = facility.getHotelFacilityGroup();
                if(hfg == hotelFacilityGroupEntity){
                    facilityResponses.add(this.mp.map(facility, FacilityResponse.class));
                }
            }
            hotelFacilityGroupResponse.setFacilities(facilityResponses);
            hotelFacilityGroupResponses.add(hotelFacilityGroupResponse);
        });

        ProductDetailResponse result = new ProductDetailResponse(hotel, rooms, hotelFacilityGroupResponses);
        return ResponseEntity.ok(result);

    }
}
