package com.example.dacn.services.impl;

import com.example.dacn.model.ProvinceEntity;
import com.example.dacn.repository.IProvinceRepository;
import com.example.dacn.services.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProvinceService implements IProvinceService {

    @Autowired
    private IProvinceRepository provinceRepository;

    @Override
    public ProvinceEntity findBy_domain(String domain) {
        return this.findBy_domain(domain);
    }
}
