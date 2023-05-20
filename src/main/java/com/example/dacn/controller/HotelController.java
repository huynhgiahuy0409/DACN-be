package com.example.dacn.controller;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.model.SearchedProductSorter;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.requestmodel.OptionFilterRequest;
import com.example.dacn.requestmodel.ProductSortRequest;
import com.example.dacn.responsemodel.*;
import com.example.dacn.services.*;
import com.example.dacn.services.impl.HotelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
}
