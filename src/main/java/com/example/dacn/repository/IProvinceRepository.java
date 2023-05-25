package com.example.dacn.repository;

import com.example.dacn.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<ProvinceEntity, Long>, JpaSpecificationExecutor<ProvinceEntity> {
    public ProvinceEntity findBy_domain(String domain);

}
