package com.example.dacn.repository;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.model.ValidSearchedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long>, JpaSpecificationExecutor<HotelEntity> {
    @Query(value = "SELECT COUNT(*) " +
            "FROM hotel h1 " +
            "         JOIN " +
            "     (SELECT h.id hotelId " +
            "      FROM hotel h " +
            "               JOIN room r on h.id = r.hotel_id " +
            "               JOIN room_benefit rb on r.id = rb.room_id " +
            "               JOIN address a on h.address_id = a.id " +
            "               JOIN province p on a.province_id = p.id " +
            "               JOIN hotel_facility as hf ON hf.hotel_id = h.id " +
            "               JOIN discount d on r.discount_id = d.id " +
            "               JOIN facility f on hf.facility_id = f.id " +
            "      WHERE p.id = :provinceId " +
            "        and (:priceFrom IS NULL OR r.rental_price >= :priceFrom) " +
            "        and (:priceTo IS NULL OR r.rental_price <= :priceTo) " +
            "        and r.max_adults = :maxAdults " +
            "        and r.max_children = :maxChildren " +
            "        AND (CASE " +
            "                 WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) " +
            "                 ELSE hf.facility_id END) " +
            "        AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END) " +
            "        AND (:discount IS NULL OR d.discount_percent > :discount) " +
            "        AND (:rate IS NULL OR h.average_points > :rate) " +
            "      group by h.id) result on result.hotelId = h1.id", nativeQuery = true)
    public Long countHotelsByPoint(@Param("rate") Double rate, @Param("discount") Double discount, @Param("checkHotelFacilities") Integer checkHotelFacilities, @Param("hotelFacilities") List<Long> hotelFacilities, @Param("checkBenefits") Integer checkBenefits, @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);

    @Query(value = "SELECT COUNT(*) " +
            "FROM hotel h1 " +
            "         JOIN " +
            "     (SELECT h.id hotelId " +
            "      FROM hotel h " +
            "               JOIN room r on h.id = r.hotel_id " +
            "               JOIN room_benefit rb on r.id = rb.room_id " +
            "               JOIN address a on h.address_id = a.id " +
            "               JOIN province p on a.province_id = p.id " +
            "               JOIN hotel_facility as hf ON hf.hotel_id = h.id " +
            "               JOIN discount d on r.discount_id = d.id " +
            "               JOIN facility f on hf.facility_id = f.id " +
            "      WHERE p.id = :provinceId " +
            "        and (:priceFrom IS NULL OR r.rental_price >= :priceFrom) " +
            "        and (:priceTo IS NULL OR r.rental_price <= :priceTo) " +
            "        and r.max_adults = :maxAdults " +
            "        and r.max_children = :maxChildren " +
            "        AND (CASE " +
            "                 WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) " +
            "                 ELSE hf.facility_id END) " +
            "        AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END) " +
            "        AND (:discount IS NULL OR d.discount_percent > :discount) " +
            "        AND (:rate IS NULL OR h.average_points > :rate) " +
            "      group by h.id) result on result.hotelId = h1.id", nativeQuery = true)
    public Long countHotelByDiscountPercent(@Param("rate") Double rate, @Param("discount") Double discount, @Param("checkHotelFacilities") Integer checkHotelFacilities, @Param("hotelFacilities") List<Long> hotelFacilities, @Param("checkBenefits") Integer checkBenefits, @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);

    @Query(value = "SELECT foundResult.hotelId, r1.id " +
            "FROM room r1 " +
            "         JOIN ( " +
            "    SELECT h.id as hotelId, h.name as hotelName, min(rental_price) as minPrice " +
            "    FROM hotel h " +
            "             JOIN address a ON h.address_id = a.id " +
            "             JOIN province p ON a.province_id = p.id " +
            "             JOIN room r ON r.hotel_id = h.id " +
            "             JOIN hotel_facility hf ON hf.hotel_id = h.id " +
            "             JOIN discount d ON r.discount_id = d.id " +
            "             JOIN room_benefit rb ON r.id = rb.room_id " +
            "    WHERE (:hotelId IS NULL OR h.id = :hotelId) " +
            "      AND (:priceFrom IS NULL OR r.rental_price >= :priceFrom) " +
            "      AND (:priceTo IS NULL OR r.rental_price <= :priceTo) " +
            "      AND r.max_adults = :maxAdults " +
            "      AND r.max_children = :maxChildren " +
            "      AND (CASE WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) ELSE hf.facility_id END) " +
            "      AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END) " +
            "      AND (:discount IS NULL OR d.discount_percent > :discount) " +
            "      AND (:rate IS NULL OR h.average_points > :rate) " +
            "    GROUP BY h.id " +
            ") foundResult ON foundResult.minprice = r1.rental_price AND r1.hotel_id = foundResult.hotelId " +
            "WHERE r1.rental_price = foundResult.minprice LIMIT 1", nativeQuery = true)
    public Object findValidSearchedProduct(@Param("hotelId") Long hotelId, @Param("rate") Double rate, @Param("discount") Double discount, @Param("checkHotelFacilities") Integer checkHotelFacilities, @Param("hotelFacilities") List<Long> hotelFacilities, @Param("checkBenefits") Integer checkBenefits, @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);

    @Query(value = "SELECT foundResult.hotelId, r1.id, hotelName, r1.rental_price, minPrice, rate " +
            "FROM room r1 " +
            "         JOIN ( " +
            "    SELECT h.id as hotelId, h.name as hotelName, MIN(r.rental_price) as minPrice, r.rental_price as rentalPrice, h.average_points as rate, r.id as roomId " +
            "    FROM hotel h " +
            "             JOIN address a ON h.address_id = a.id " +
            "             JOIN province p ON a.province_id = p.id " +
            "             JOIN room r ON r.hotel_id = h.id " +
            "             JOIN hotel_facility hf ON hf.hotel_id = h.id " +
            "             JOIN discount d ON r.discount_id = d.id " +
            "             JOIN room_benefit rb ON r.id = rb.room_id " +
            "    WHERE (:provinceId IS NULL OR p.id = :provinceId) " +
            "      AND (:priceFrom IS NULL OR r.rental_price >= :priceFrom) " +
            "      AND (:priceTo IS NULL OR r.rental_price <= :priceTo) " +
            "      AND r.max_adults = :maxAdults " +
            "      AND r.max_children = :maxChildren " +
            "      AND (CASE WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) ELSE hf.facility_id END) " +
            "      AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END) " +
            "      AND (:discount IS NULL OR d.discount_percent > :discount) " +
            "      AND (:rate IS NULL OR h.average_points > :rate) " +
            "    GROUP BY h.id " +
            ") foundResult ON foundResult.minPrice = r1.rental_price AND r1.hotel_id = foundResult.hotelId AND foundResult.roomId = r1.id " +
            "WHERE r1.rental_price = foundResult.minPrice and roomId = r1.id " +
            "ORDER BY CASE WHEN :dir = 'asc' AND :orderBy = 'price' THEN minPrice END ASC, " +
            "         CASE WHEN :dir = 'asc' AND :orderBy = 'name' THEN hotelName END ASC, " +
            "         CASE WHEN :dir = 'asc' AND :orderBy = 'rate' THEN rate END ASC, " +
            "         CASE WHEN :dir =  'asc' THEN hotelId END ASC, " +
            "         CASE WHEN :dir = 'desc' AND :orderBy = 'price' THEN minPrice END DESC, " +
            "         CASE WHEN :dir = 'desc' AND :orderBy = 'name' THEN hotelName END DESC, " +
            "         CASE WHEN :dir = 'desc' AND :orderBy = 'rate' THEN rate END DESC, " +
            "         CASE WHEN :dir = 'desc' THEN hotelId END DESC"
            , nativeQuery = true)
    public List<Object> findValidRelativeSearchedProduct(@Param("rate") Double rate, @Param("discount") Double discount, @Param("checkHotelFacilities") Integer checkHotelFacilities, @Param("hotelFacilities") List<Long> hotelFacilities, @Param("checkBenefits") Integer checkBenefits, @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren, @Param("orderBy") String orderBy, @Param("dir") String dir, Pageable pageable);

//    FROM room r1
//    JOIN
//            (SELECT h.id as hotelId, h.name as hotelName, min(rental_price) as minPrice, r.id as roomId, r.rental_price, r.max_adults, r.max_children, h.average_points as rate, d.discount_percent as discountPercent
//    FROM hotel h
//    JOIN address docker-compose.yml on h.address_id = docker-compose.yml.id
//    JOIN province p on docker-compose.yml.province_id = p.id
//    JOIN room r ON r.hotel_id = h.id
//    JOIN hotel_facility as hf ON hf.hotel_id = h.id
//    JOIN discount d on r.discount_id = d.id
//    WHERE p.id = :provinceId
//    and (:priceFrom IS NULL OR r.rental_price >= :priceFrom)
//    and (:priceTo IS NULL OR r.rental_price <= :priceTo)
//    and r.max_adults = :maxAdults
//    and r.max_children = :maxChildren
//    AND (CASE WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) ELSE hf.facility_id END)
//    AND d.discount_percent > 30
//    AND h.average_points > 8
//    GROUP BY h.id) foundResult ON foundResult.minprice = r1.rental_price and r1.hotel_id = foundResult.hotelId and r1.id = foundResult.roomId
//    where r1.rental_price = foundResult.minPrice
//    group by foundResult.hotelId, foundResult.minPrice, rate, hotelName
//    ORDER BY hotelId ASC;
}
