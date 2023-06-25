package com.example.dacn.services.impl;

import com.example.dacn.entity.FacilityEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.repository.IFacilityRepository;
import com.example.dacn.requestmodel.OptionFilterRequest;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import com.example.dacn.responsemodel.FilterOptionResponse;
import com.example.dacn.services.IFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class FacilityService implements IFacilityService {
    @Autowired
    private IFacilityRepository facilityRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<FilterOptionItemResponse> findHotelFacilities(ProductFilterRequest productFilterRequest) {
        Long hotelId = productFilterRequest.getValue();
        HotelEntity searchedHotel = this.hotelRepository.getOne(hotelId);
        ProvinceEntity province = searchedHotel.getAddress().getProvince();
        Pageable p = PageRequest.of(0, 20);
        List<Long> hotelFacilities = Arrays.asList();
        List<Long> benefits = Arrays.asList();
        Integer checkHotelFacilityIds = 0;
        Integer checkBenefits = 0;
        OptionFilterRequest optionFilterRequest = productFilterRequest.getOptionFilter();
        if (optionFilterRequest != null && optionFilterRequest.getHotelFacilities() != null && optionFilterRequest.getHotelFacilities().size() > 0) {
            benefits = optionFilterRequest.getHotelFacilities();
            checkHotelFacilityIds = optionFilterRequest.getHotelFacilities().size();
        }
        if (optionFilterRequest != null && optionFilterRequest.getBenefits() != null && optionFilterRequest.getBenefits().size() > 0) {
            benefits = optionFilterRequest.getBenefits();
            checkBenefits = optionFilterRequest.getBenefits().size();
        }
        List<FilterOptionItemResponse> result = new LinkedList();
        for (Object hotelFacilityOption : this.facilityRepository.findHotelFacilityOptions(productFilterRequest.getOptionFilter().getGuestRating(), productFilterRequest.getOptionFilter().getDiscount(), null, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren(), p)) {
            Object[] responses = Arrays.stream((Object[]) hotelFacilityOption).map(o -> {
                return o.toString();
            }).toArray(Object[]::new);
            FilterOptionItemResponse item = new FilterOptionItemResponse(responses[0], responses[1].toString().trim(), Long.parseLong(responses[2].toString().trim()));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<FacilityEntity> findAllByHotelId(Long hotelId) {
        return null;
    }
}
