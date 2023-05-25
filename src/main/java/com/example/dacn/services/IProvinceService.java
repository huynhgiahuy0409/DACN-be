package com.example.dacn.services;

import com.example.dacn.entity.ProvinceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IProvinceService {
    ProvinceEntity findBy_domain(String domain);

    List<ProvinceEntity> findAll(Specification<ProvinceEntity> specification, Pageable pageable);
}
