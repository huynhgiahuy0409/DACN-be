package com.example.dacn.repository;

import com.example.dacn.model.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IBenefitRepository extends JpaRepository<BenefitEntity, Long>, JpaSpecificationExecutor<BenefitEntity> {
}
