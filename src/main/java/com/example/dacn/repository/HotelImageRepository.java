package com.example.dacn.repository;

import com.example.dacn.model.AddressEntity;
import com.example.dacn.model.HotelImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImageEntity, Long> {
}
