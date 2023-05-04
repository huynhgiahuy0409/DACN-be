package com.example.dacn.services;

import com.example.dacn.model.BenefitEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IBenefitService {

    public List<BenefitEntity> findAll(Specification<BenefitEntity> benefitEntitySpecification);

}
