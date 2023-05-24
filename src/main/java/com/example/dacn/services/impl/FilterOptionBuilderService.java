package com.example.dacn.services.impl;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.repository.IBenefitRepository;
import com.example.dacn.repository.IFacilityRepository;
import com.example.dacn.requestmodel.OptionFilterRequest;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import com.example.dacn.services.IFilterOptionBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class FilterOptionBuilderService implements IFilterOptionBuilderService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private IBenefitRepository benefitRepository;
    @Autowired
    IFacilityRepository facilityRepository;

    @Override
    public List<FilterOptionItemResponse> findBenefitsOptions(ProductFilterRequest productFilterRequest) {
        Pageable p = PageRequest.of(0, 20);
        return this.buildFilterOptions(productFilterRequest, p, "benefit");
    }

    @Override
    public List<FilterOptionItemResponse> findUserRateOptions(ProductFilterRequest productFilterRequest) {
        Pageable p = PageRequest.of(0, 20);
        return this.buildFilterOptions(productFilterRequest, p, "rate");
    }

    @Override
    public List<FilterOptionItemResponse> findHotelFacilitiesOptions(ProductFilterRequest productFilterRequest) {
        Pageable p = PageRequest.of(0, 20);
        return this.buildFilterOptions(productFilterRequest, p, "hotelFacility");
    }

    @Override
    public List<FilterOptionItemResponse> findDiscountOptions(ProductFilterRequest productFilterRequest) {
        Pageable p = PageRequest.of(0, 20);
        return this.buildFilterOptions(productFilterRequest, p, "discount");
    }

    private List<FilterOptionItemResponse> buildFilterOptions(ProductFilterRequest productFilterRequest, Pageable pageable, String type) {
        System.out.println(productFilterRequest);
        Long hotelId = productFilterRequest.getValue();
        HotelEntity searchedHotel = this.hotelRepository.getOne(hotelId);
        ProvinceEntity province = searchedHotel.getAddress().getProvince();
        List<Long> hotelFacilities =  new ArrayList<>();
        List<Long> benefits = new ArrayList<>();
        Integer checkHotelFacilityIds = 0;
        Integer checkBenefits = 0;
        OptionFilterRequest optionFilterRequest = productFilterRequest.getOptionFilter();
        if (optionFilterRequest != null && optionFilterRequest.getHotelFacilities() != null && optionFilterRequest.getHotelFacilities().size() > 0) {
            hotelFacilities.addAll(optionFilterRequest.getHotelFacilities());
            checkHotelFacilityIds = hotelFacilities.size();
        }
        if (optionFilterRequest != null && optionFilterRequest.getBenefits() != null && optionFilterRequest.getBenefits().size() > 0) {
            benefits.addAll(optionFilterRequest.getBenefits());
            checkBenefits = benefits.size();
        }
        List<Object> foundQueryList;
        List<FilterOptionItemResponse> result = new LinkedList<>();
        System.out.println(checkHotelFacilityIds);
        System.out.println(checkBenefits);
        if (type.equals("benefit")) {
            foundQueryList = this.benefitRepository.findBenefitOptions(productFilterRequest.getOptionFilter().getGuestRating(), productFilterRequest.getOptionFilter().getDiscount(), checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren(), pageable);
            foundQueryList.forEach(item -> {
                Object[] objects = (Object[]) item;
                FilterOptionItemResponse filterOptionItemResponse = new FilterOptionItemResponse(objects[0], objects[1].toString(), Long.parseLong(objects[2].toString()));
                result.add(filterOptionItemResponse);
            });
        } else if (type.equals("rate")) {
            Double discountPercent = productFilterRequest.getOptionFilter().getDiscount() != null ? productFilterRequest.getOptionFilter().getDiscount() : null;
            Long countHotelUp7 = this.hotelRepository.countHotelsByPoint(Double.parseDouble("7"), discountPercent, checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
            Long countHotelUp8 = this.hotelRepository.countHotelsByPoint(Double.parseDouble("8"), discountPercent, checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
            Long countHotelUp9 = this.hotelRepository.countHotelsByPoint(Double.parseDouble("9"), discountPercent, checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
            result.add(new FilterOptionItemResponse("7", "7+ Rất tốt", countHotelUp7));
            result.add(new FilterOptionItemResponse("8", "8+ Trên cả tuyệt vời", countHotelUp8));
            result.add(new FilterOptionItemResponse("9", "9+ Xuất sắc", countHotelUp9));
        } else if (type.equals("hotelFacility")) {
            foundQueryList = this.facilityRepository.findHotelFacilityOptions(productFilterRequest.getOptionFilter().getGuestRating(), productFilterRequest.getOptionFilter().getDiscount(), null, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren(), pageable);
            foundQueryList.forEach(item -> {
                Object[] objects = (Object[]) item;
                FilterOptionItemResponse filterOptionItemResponse = new FilterOptionItemResponse(objects[0], objects[1].toString(), Long.parseLong(objects[2].toString()));
                result.add(filterOptionItemResponse);
            });
        } else if (type.equals("discount")) {
            Double ratePoint = productFilterRequest.getOptionFilter().getGuestRating() != null ? productFilterRequest.getOptionFilter().getGuestRating() : null;
            Long countUpper10PercentDiscount = this.hotelRepository.countHotelByDiscountPercent(ratePoint, Double.parseDouble("10"), checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
            Long countUpper50PercentDiscount = this.hotelRepository.countHotelByDiscountPercent(ratePoint, Double.parseDouble("50"), checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
            FilterOptionItemResponse i1 = new FilterOptionItemResponse("10", "Giảm từ 30% trở lên", countUpper10PercentDiscount);
            FilterOptionItemResponse i2 = new FilterOptionItemResponse("50", "Giảm từ 50% trở lên", countUpper50PercentDiscount);
            result.add(i1);
            result.add(i2);
        } else {
            foundQueryList = new ArrayList<>();
        }
        return result;
    }

}
