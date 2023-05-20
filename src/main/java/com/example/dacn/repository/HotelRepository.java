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
    @Query(value = "SELECT COUNT(*)\n" +
            "FROM hotel h1\n" +
            "         JOIN\n" +
            "     (SELECT h.id hotelId\n" +
            "      FROM hotel h\n" +
            "               JOIN room r on h.id = r.hotel_id\n" +
            "               JOIN room_benefit rb on r.id = rb.room_id\n" +
            "               JOIN address a on h.address_id = a.id\n" +
            "               JOIN province p on a.province_id = p.id\n" +
            "               JOIN hotel_facility as hf ON hf.hotel_id = h.id\n" +
            "               JOIN discount d on r.discount_id = d.id\n" +
            "               JOIN facility f on hf.facility_id = f.id\n" +
            "      WHERE p.id = :provinceId\n" +
            "        and (:priceFrom IS NULL OR r.rental_price >= :priceFrom)\n" +
            "        and (:priceTo IS NULL OR r.rental_price <= :priceTo)\n" +
            "        and r.max_adults = :maxAdults\n" +
            "        and r.max_children = :maxChildren\n" +
            "        AND (CASE\n" +
            "                 WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities)\n" +
            "                 ELSE hf.facility_id END)\n" +
            "        AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END)\n" +
            "        AND (:discount IS NULL OR d.discount_percent > :discount)\n" +
            "        AND (:rate IS NULL OR h.average_points > :rate)\n" +
            "      group by h.id) result on result.hotelId = h1.id", nativeQuery = true)
    public Long countHotelsByPoint(@Param("rate") Double rate,@Param("discount") Double discount ,@Param("checkHotelFacilities") Integer checkHotelFacilities,  @Param("hotelFacilities") List<Long> hotelFacilities,@Param("checkBenefits") Integer checkBenefits,  @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);

    @Query(value = "SELECT COUNT(*)\n" +
            "FROM hotel h1\n" +
            "         JOIN\n" +
            "     (SELECT h.id hotelId\n" +
            "      FROM hotel h\n" +
            "               JOIN room r on h.id = r.hotel_id\n" +
            "               JOIN room_benefit rb on r.id = rb.room_id\n" +
            "               JOIN address a on h.address_id = a.id\n" +
            "               JOIN province p on a.province_id = p.id\n" +
            "               JOIN hotel_facility as hf ON hf.hotel_id = h.id\n" +
            "               JOIN discount d on r.discount_id = d.id\n" +
            "               JOIN facility f on hf.facility_id = f.id\n" +
            "      WHERE p.id = :provinceId\n" +
            "        and (:priceFrom IS NULL OR r.rental_price >= :priceFrom)\n" +
            "        and (:priceTo IS NULL OR r.rental_price <= :priceTo)\n" +
            "        and r.max_adults = :maxAdults\n" +
            "        and r.max_children = :maxChildren\n" +
            "        AND (CASE\n" +
            "                 WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities)\n" +
            "                 ELSE hf.facility_id END)\n" +
            "        AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END)\n" +
            "        AND (:discount IS NULL OR d.discount_percent > :discount)\n" +
            "        AND (:rate IS NULL OR h.average_points > :rate)\n" +
            "      group by h.id) result on result.hotelId = h1.id", nativeQuery = true)
    public Long countHotelByDiscountPercent(@Param("rate") Double rate,@Param("discount") Double discount ,@Param("checkHotelFacilities") Integer checkHotelFacilities,  @Param("hotelFacilities") List<Long> hotelFacilities,@Param("checkBenefits") Integer checkBenefits,  @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren);

    @Query("SELECT new com.example.dacn.model.ValidSearchedProduct(h.id, r.id) " +
            "FROM HotelEntity h JOIN RoomEntity r ON h.id = r.hotel.id " +
            "JOIN DiscountEntity d ON r.discount.id = d.id " +
            "WHERE (:priceFrom IS NULL OR r.rentalPrice >= :priceFrom) " +
            "AND (:priceTo IS NULL OR r.rentalPrice <= :priceTo) " +
            "AND h.id = :hotelId " +
            "AND r.maxAdults = :maxAdults " +
            "AND r.maxChildren = :maxChildren  " +
            "ORDER BY r.rentalPrice DESC"
    )
    public Page<ValidSearchedProduct> findValidSearchedProduct(@Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("hotelId") Long hotelId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren, Pageable pageable);

    @Query(value = "SELECT foundResult.hotelId, r1.id, hotelName, r1.rental_price, rate\n" +
            "FROM room r1\n" +
            "         JOIN\n" +
            "     (SELECT h.id as hotelId, h.name as hotelName, min(rental_price) as minPrice, h.average_points as rate\n, r.id as roomId" +
            "      FROM hotel h\n" +
            "               JOIN address a on h.address_id = a.id\n" +
            "               JOIN province p on a.province_id = p.id\n" +
            "               JOIN room r ON r.hotel_id = h.id\n JOIN benefit" +
            "               JOIN hotel_facility as hf ON hf.hotel_id = h.id\n " +
            "               JOIN discount as d ON r.discount_id = d.id " +
            "               JOIN room_benefit rb on r.id = rb.room_id" +
            "      WHERE p.id = :provinceId\n" +
            "        and (:priceFrom IS NULL OR r.rental_price >= :priceFrom)\n" +
            "        and (:priceTo IS NULL OR r.rental_price <= :priceTo)\n" +
            "        and r.max_adults = :maxAdults\n" +
            "        and r.max_children = :maxChildren\n" +
            "        AND (CASE WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) ELSE hf.facility_id END) \n " +
            "         AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END) \n " +
            "        AND (:discount IS NULL OR d.discount_percent > :discount) " +
            "        AND (:rate IS NULL OR h.average_points > :rate) " +
            "GROUP BY h.id) foundResult ON foundResult.minprice = r1.rental_price AND r1.hotel_id = foundResult.hotelId AND foundResult.roomId = r1.id where r1.rental_price = foundResult.minprice GROUP BY foundResult.hotelId, foundResult.minPrice, rate, hotelName " +
            "ORDER BY " +
            "CASE WHEN :dir ='ASc' THEN " +
            "CASE WHEN :orderBy = 'price' THEN minPrice WHEN :orderBy = 'name' THEN hotelName WHEN :orderBy = 'rate' THEN rate ELSE hotelID END END ASC, " +
            "CASE WHEN :dir ='desc' THEN " +
            "CASE WHEN :orderBy = 'price' THEN minPrice WHEN :orderBy = 'name' THEN hotelName WHEN :orderBy = 'rate' THEN rate ELSE hotelID END END DESC", nativeQuery = true)
    public List<Object> findValidRelativeSearchedProduct(@Param("rate") String rate,@Param("discount") String discount ,@Param("checkHotelFacilities") Integer checkHotelFacilities,  @Param("hotelFacilities") List<Long> hotelFacilities,@Param("checkBenefits") Integer checkBenefits,  @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren, @Param("orderBy") String orderBy, @Param("dir") String dir, Pageable pageable);

//    FROM room r1
//    JOIN
//            (SELECT h.id as hotelId, h.name as hotelName, min(rental_price) as minPrice, r.id as roomId, r.rental_price, r.max_adults, r.max_children, h.average_points as rate, d.discount_percent as discountPercent
//    FROM hotel h
//    JOIN address a on h.address_id = a.id
//    JOIN province p on a.province_id = p.id
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
