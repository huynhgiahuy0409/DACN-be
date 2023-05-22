package com.example.dacn.controller;

import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import com.example.dacn.services.IFacilityService;
import com.example.dacn.services.IFilterOptionBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/filter")
@CrossOrigin("http://localhost:4200")
public class FilterController {

    @Autowired
    private IFilterOptionBuilderService filterOptionBuilderService;
    @PostMapping("/option/benefits")
    public ResponseEntity<List<FilterOptionItemResponse>> findBenefitsOptions(@RequestBody ProductFilterRequest productFilterRequest){
        return ResponseEntity.ok(this.filterOptionBuilderService.findBenefitsOptions(productFilterRequest));
    }

    @PostMapping("/option/user-rates")
    public ResponseEntity findUserRateOptions(@RequestBody ProductFilterRequest productFilterRequest){
        return ResponseEntity.ok(this.filterOptionBuilderService.findUserRateOptions(productFilterRequest));
    }
    @PostMapping("/option/hotel-facilities")
    public ResponseEntity findHotelFacilitiesOptions(@RequestBody ProductFilterRequest productFilterRequest){
        return ResponseEntity.ok(this.filterOptionBuilderService.findHotelFacilitiesOptions(productFilterRequest));
    }

    @PostMapping("/option/discounts")
    public ResponseEntity findDiscountOptions(@RequestBody ProductFilterRequest productFilterRequest){
        return ResponseEntity.ok(this.filterOptionBuilderService.findDiscountOptions(productFilterRequest));
    }
}
