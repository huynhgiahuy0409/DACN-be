package com.example.dacn.services.impl;

import com.example.dacn.entity.DistrictEntity;
import com.example.dacn.repository.DistrictRepository;
import com.example.dacn.services.IDistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DistrictService implements IDistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<DistrictEntity> findAll(Specification<DistrictEntity> spec, Pageable pageable) {
        return this.districtRepository.findAll(spec, pageable).getContent();
    }
}
