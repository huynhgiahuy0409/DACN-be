package com.example.dacn.services.impl;

import com.example.dacn.model.BenefitEntity;
import com.example.dacn.repository.IBenefitRepository;
import com.example.dacn.services.IBenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BenefitService implements IBenefitService {

    @Autowired
    private IBenefitRepository benefitRepository;
    @Override
    public List<BenefitEntity> findAll(Specification<BenefitEntity> benefitEntitySpecification) {
        return this.benefitRepository.findAll(benefitEntitySpecification);
    }
}
