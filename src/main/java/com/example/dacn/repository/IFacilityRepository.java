package com.example.dacn.repository;

import com.example.dacn.entity.FacilityEntity;
import com.example.dacn.responsemodel.FilterOptionItemResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFacilityRepository extends JpaRepository<FacilityEntity, Long> {
    @Query(value = "SELECT result.facilityId, f.name , count(result.facilityId) countHotel FROM facility f JOIN (SELECT h.id hotelId, hf.facility_id as facilityId\n" +
            "FROM hotel h\n" +
            "         JOIN room r on h.id = r.hotel_id\n" +
            "         JOIN room_benefit rb on r.id = rb.room_id\n" +
            "         JOIN address a on h.address_id = a.id\n" +
            "         JOIN province p on a.province_id = p.id\n" +
            "         JOIN hotel_facility as hf ON hf.hotel_id = h.id\n" +
            "         JOIN discount d on r.discount_id = d.id\n" +
            "         JOIN facility f on hf.facility_id = f.id\n" +
            "WHERE p.id = :provinceId\n" +
            "  and (:priceFrom IS NULL OR r.rental_price >= :priceFrom)\n" +
            "  and (:priceTo IS NULL OR r.rental_price <= :priceTo)\n" +
            "  and r.max_adults = :maxAdults\n" +
            "  and r.max_children = :maxChildren\n" +
            "  AND (CASE WHEN :checkHotelFacilities != 0 THEN hf.facility_id IN (:hotelFacilities) ELSE hf.facility_id END)\n" +
            "  AND (CASE WHEN :checkBenefits != 0 THEN rb.benefit_id IN (:benefits) ELSE rb.benefit_id END)\n" +
            "  AND (:discount IS NULL OR d.discount_percent > :discount)\n" +
            "  AND (:rate IS NULL OR h.average_points > :rate)\n" +
            "group by hf.facility_id, h.id) result on f.id = result.facilityId group by result.facilityId ORDER BY countHotel DESC", nativeQuery = true)
    public List<Object> findHotelFacilityOptions(@Param("rate") String rate,@Param("discount") String discount ,@Param("checkHotelFacilities") Integer checkHotelFacilities,  @Param("hotelFacilities") List<Long> hotelFacilities,@Param("checkBenefits") Integer checkBenefits,  @Param("benefits") List<Long> benefits, @Param("priceFrom") Double priceFrom, @Param("priceTo") Double priceTo, @Param("provinceId") Long provinceId, @Param("maxAdults") Integer maxAdults, @Param("maxChildren") Integer maxChildren, Pageable pageable);
}
