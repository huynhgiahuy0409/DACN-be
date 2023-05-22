package com.example.dacn.repository;

import com.example.dacn.entity.BenefitEntity;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBenefitRepository extends JpaRepository<BenefitEntity, Long>, JpaSpecificationExecutor<BenefitEntity> {

//        @Query(value = "SELECT b.name, b.id, count_hotel from elhnhxqj_rental_car_secondary.benefit b JOIN (SELECT rb.benefit_id, count(r.hotel_id) as count_hotel FROM elhnhxqj_rental_car_secondary.room_benefit rb join elhnhxqj_rental_car_secondary.room r on rb.room_id = r.id where r.max_adults = 2 group by rb.benefit_id) rb_r on rb_r.benefit_id = b.id;",
//                nativeQuery = true)
//        public List<ABCD> findBenefitOptions(Integer maxAdults);

        @Query(value = "SELECT result.benefitId, b.name, count(result.benefitId) countHotel FROM benefit b JOIN (SELECT h.id hotelId, rb.benefit_id benefitId\n" +
                "FROM hotel h\n" +
                "         JOIN room r on h.id = r.hotel_id\n" +
                "         JOIN room_benefit rb on r.id = rb.room_id\n" +
                "         JOIN address a on h.address_id = a.id\n" +
                "         JOIN province p on a.province_id = p.id\n" +
                "         JOIN hotel_facility as hf ON hf.hotel_id = h.id\n" +
                "         JOIN discount d on r.discount_id = d.id\n" +
                "WHERE p.id = :provinceId\n" +
                "  and (:priceFrom IS NULL OR r.rental_price >= :priceFrom)\n" +
                "  and (:priceTo IS NULL OR r.rental_price <= :priceTo)\n" +
                "  and r.max_adults = :maxAdults\n" +
                "  and r.max_children = :maxChildren\n" +
                "  AND (CASE WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) ELSE hf.facility_id END)\n" +
                "  AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END)\n" +
                "  AND (:discount IS NULL OR d.discount_percent > :discount)\n" +
                "  AND (:rate IS NULL OR h.average_points > :rate)\n" +
                "group by rb.benefit_id, h.id) result on b.id = result.benefitId group by result.benefitId ORDER BY countHotel DESC", nativeQuery = true)
        public List<Object> findBenefitOptions(@Param("rate") String rate,@Param("discount") String discount ,@Param("checkHotelFacilities") Integer checkHotelFacilities,  @Param("hotelFacilities") List<Long> hotelFacilities,@Param("checkBenefits") Integer checkBenefits,  @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren, Pageable pageable);

}
