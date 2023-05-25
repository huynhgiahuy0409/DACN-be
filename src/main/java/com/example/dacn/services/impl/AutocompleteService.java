package com.example.dacn.services.impl;

import com.example.dacn.dto.response.AutocompleteSearchResponse;
import com.example.dacn.entity.AddressEntity;
import com.example.dacn.entity.DistrictEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.IAutocompleteService;
import com.example.dacn.services.IDistrictService;
import com.example.dacn.services.IProvinceService;
import com.example.dacn.specification.builder.DistrictSpecificationBuilder;
import com.example.dacn.specification.builder.HotelSpecificationBuilder;
import com.example.dacn.specification.builder.ProvinceSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutocompleteService implements IAutocompleteService {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private IProvinceService provinceService;

    @Autowired
    private IDistrictService districtService;

    @Override
    public List<AutocompleteSearchResponse> buildAnAutocompletes(String value) {
        List<AutocompleteSearchResponse> data = new ArrayList<>();
        HotelSpecificationBuilder hotelSpecBuilder = new HotelSpecificationBuilder();
        ProvinceSpecificationBuilder provinceSpecBuilder = new ProvinceSpecificationBuilder();
        DistrictSpecificationBuilder districtSpecBuilder = new DistrictSpecificationBuilder();
        Specification<HotelEntity> hotelSpec = hotelSpecBuilder.with("name", value, "like").build();
        Specification<ProvinceEntity> provinceSpec = provinceSpecBuilder.with("_domain", value, "like").build();
        Specification<DistrictEntity> districtSpec = districtSpecBuilder.with("_name", value, "like").build();
        Pageable pageable = PageRequest.of(0, 4);
        List<HotelEntity> hotels = this.hotelService.findAll(hotelSpec, pageable);
        List<ProvinceEntity> provinces = this.provinceService.findAll(provinceSpec, pageable);
        List<DistrictEntity> districts = this.districtService.findAll(districtSpec, pageable);
        List<AutocompleteSearchResponse> hotelData = hotels.stream().map(hotel ->  new AutocompleteSearchResponse(hotel.getName() + ", " + this.buildAnAdressString(hotel.getAddress()) , "Khách sạn", hotel.getId(), "hotel")).collect(Collectors.toList());
        List<AutocompleteSearchResponse> provinceData = provinces.stream().map(province -> new AutocompleteSearchResponse(province.get_domain(), "Thành phố", province.getId(), "province")).collect(Collectors.toList());
        List<AutocompleteSearchResponse> districtData = districts.stream().map(district -> {
            ProvinceEntity province = district.getProvince();
            return new AutocompleteSearchResponse(district.get_name() + ", " + province.get_domain(), "Quận", district.getId(), "district");
        }).collect(Collectors.toList());
        data.addAll(hotelData);
        data.addAll(provinceData);
        data.addAll(districtData);
        return data;
    }
    private String buildAnAdressString(AddressEntity add){
        return add.getDistrict().get_name() + ", " + add.getProvince().get_domain();
    }
}
