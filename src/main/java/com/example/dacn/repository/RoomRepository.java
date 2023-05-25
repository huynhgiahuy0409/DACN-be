package com.example.dacn.repository;

import com.example.dacn.entity.RoomEntity;
import com.example.dacn.requestmodel.ProductFilterRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long>, JpaSpecificationExecutor<RoomEntity> {

    RoomEntity findFirstByHotelIdOrderByFinalPriceAsc(Long hotelId);

    @Query(value = "SELECT MIN(minPrice)\n" +
            "FROM room r1 JOIN\n" +
            "    (SELECT h.id as hotelId, h.name as hotelName, min(rental_price) as minPrice, h.average_points as rate\n" +
            "    FROM hotel h JOIN address a on h.address_id = a.id\n" +
            "    JOIN province p on a.province_id = p.id\n" +
            "    JOIN room r ON r.hotel_id = h.id\n" +
            "    WHERE p.id = :provinceId and r.max_adults = :maxAdults and r.max_children = :maxChildren\n" +
            "    GROUP BY h.id) foundResult ON foundResult.minprice = r1.rental_price and r1.hotel_id = foundResult.hotelId where r1.rental_price = r1.rental_price ", nativeQuery = true)
    Double findMinPriceByFilter(@Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);
    @Query(value = "SELECT MAX(minPrice)\n" +
            "FROM room r1 JOIN\n" +
            "    (SELECT h.id as hotelId, h.name as hotelName, min(rental_price) as minPrice, h.average_points as rate\n" +
            "    FROM hotel h JOIN address a on h.address_id = a.id\n" +
            "    JOIN province p on a.province_id = p.id\n" +
            "    JOIN room r ON r.hotel_id = h.id\n" +
            "    WHERE p.id = :provinceId and r.max_adults = :maxAdults and r.max_children = :maxChildren\n" +
            "    GROUP BY h.id) foundResult ON foundResult.minprice = r1.rental_price and r1.hotel_id = foundResult.hotelId where r1.rental_price = r1.rental_price", nativeQuery = true)
    Double findMaxPriceByFilter(@Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);

    @Query(value = "SELECT r.id, r.rental_price  FROM room r join hotel h on r.hotel_id = h.id where r.max_adults >= :maxAdults and r.max_children >= :maxChildren and h.id = :hotelId ORDER BY  r.rental_price ASC", nativeQuery = true)
    List<Long> findAllValidRoom(@Param("hotelId") Long hotelId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren, Pageable p);

}