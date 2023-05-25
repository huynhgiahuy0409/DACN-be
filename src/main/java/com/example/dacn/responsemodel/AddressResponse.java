package com.example.dacn.responsemodel;

import lombok.Data;

@Data
public class AddressResponse {
    private Long id;
    private String street;
    private String province;
    private String district;
    private String ward;

    public AddressResponse(Long id, String street, String province, String district, String ward) {
        this.id = id;
        this.street = street;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    public AddressResponse() {
    }
}
