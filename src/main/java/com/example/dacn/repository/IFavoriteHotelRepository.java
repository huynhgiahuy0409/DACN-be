package com.example.dacn.repository;

import com.example.dacn.entity.FavoriteHotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFavoriteHotelRepository extends JpaRepository<FavoriteHotelEntity, Long> {
    List<FavoriteHotelEntity> findAllByUserUsername(String username);

    FavoriteHotelEntity findFirstByUserUsernameAndHotelId(String username, Long hotelId);
}
