package com.example.dacn.services;

import com.example.dacn.entity.DistrictEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IDistrictService {
    List<DistrictEntity> findAll(Specification<DistrictEntity> spec, Pageable pageable);
}
