package com.example.dacn.services;

import com.example.dacn.model.ProvinceEntity;

public interface IProvinceService {
    ProvinceEntity findBy_domain(String domain);

}
