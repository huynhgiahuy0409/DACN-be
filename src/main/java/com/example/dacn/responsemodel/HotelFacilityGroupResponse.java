package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.Set;

@Data
public class HotelFacilityGroupResponse {
    private Long id;
    private String name;
    private Set<FacilityResponse> facilities;
}
