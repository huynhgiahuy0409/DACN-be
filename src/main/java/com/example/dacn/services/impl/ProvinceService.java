package com.example.dacn.services.impl;

import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.repository.IProvinceRepository;
import com.example.dacn.services.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService implements IProvinceService {

    @Autowired
    private IProvinceRepository provinceRepository;

    @Override
    public ProvinceEntity findBy_domain(String domain) {
        return this.provinceRepository.findBy_domain(domain);
    }

    @Override
    public List<ProvinceEntity> findAll(Specification<ProvinceEntity> specification, Pageable pageable) {
        return this.provinceRepository.findAll(specification, pageable).getContent();
    }


}
