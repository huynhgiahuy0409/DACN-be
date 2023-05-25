package com.example.dacn.services;

import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;

import java.util.List;

public interface IFacilityService {

    List<FilterOptionItemResponse> findHotelFacilities(ProductFilterRequest productFilterRequest);

}
