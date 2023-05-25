package com.example.dacn.services;

import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IFilterOptionBuilderService {

    List<FilterOptionItemResponse> findBenefitsOptions(ProductFilterRequest productFilterRequest);
    List<FilterOptionItemResponse> findUserRateOptions(ProductFilterRequest productFilterRequest);
    List<FilterOptionItemResponse> findHotelFacilitiesOptions(ProductFilterRequest productFilterRequest);
    List<FilterOptionItemResponse> findDiscountOptions(ProductFilterRequest productFilterRequest);
}
