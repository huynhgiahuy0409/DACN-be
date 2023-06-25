package com.example.dacn.controller;

import com.example.dacn.dto.HotelFacilityGroupDTO;
import com.example.dacn.entity.FacilityEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.HotelFacilityGroupEntity;
import com.example.dacn.responsemodel.FacilityResponse;
import com.example.dacn.responsemodel.HotelFacilityGroupResponse;
import com.example.dacn.responsemodel.HotelResponse;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.impl.FacilityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/facility")
@CrossOrigin("http://localhost:4200")
public class FacilityController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ModelMapper mp;
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity findAllByHotel(@PathVariable Long hotelId) throws Exception {
        HotelEntity foundHotel = this.hotelService.findById(hotelId);
        Set<FacilityEntity> facilities = foundHotel.getFacilities();
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

        return ResponseEntity.ok(hotelFacilityGroupResponses);
    }

}
