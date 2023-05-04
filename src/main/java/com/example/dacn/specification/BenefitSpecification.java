package com.example.dacn.specification;

import com.example.dacn.model.BenefitEntity;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

public class BenefitSpecification {
    public static Specification<BenefitEntity> distinctBenefitsByHotel(Long hotelId) {
        return (root, query, cb) -> {
            // Tạo một subquery để lấy danh sách tất cả các room của hotel
            Subquery<RoomEntity> roomSubquery = query.subquery(RoomEntity.class);
            Root<RoomEntity> roomRoot = roomSubquery.from(RoomEntity.class);
            roomSubquery.select(roomRoot);
            roomSubquery.where(cb.equal(roomRoot.get("hotel").get("id"), hotelId));

            // Sử dụng subquery để lấy danh sách các benefit liên quan đến các room của hotel
            query.distinct(true);
            Join<BenefitEntity, RoomEntity> roomJoin = root.join("rooms");
            return cb.in(roomJoin).value(roomSubquery);
        };
    }
}
