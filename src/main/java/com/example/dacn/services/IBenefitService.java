package com.example.dacn.services;

import com.example.dacn.entity.BenefitEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IBenefitService {

    public List<BenefitEntity> findAll(Specification<BenefitEntity> benefitEntitySpecification);

}
