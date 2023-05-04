package com.example.dacn.repository;

import com.example.dacn.model.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<ProvinceEntity, Long> {
    public ProvinceEntity findBy_domain(String domain);
}
