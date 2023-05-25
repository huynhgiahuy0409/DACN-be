package com.example.dacn.specification;

import com.example.dacn.entity.BenefitEntity;
import com.example.dacn.entity.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

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
//    public static Specification<FilterOptionResponse> getBenefitStatisticsSpecification() {
//        return (root, query, cb) -> {
//            Subquery<Long> subquery = query.subquery(Long.class);
//            Root<RoomEntity> subqueryRoot = subquery.from(RoomEntity.class);
//            Join<RoomEntity, BenefitEntity> roomBenefitJoin = subqueryRoot.join("benefits");
//            subquery.select(cb.count(roomBenefitJoin.get("hotel").get("id")))
//                    .where(cb.equal(roomBenefitJoin.get("maxAdults"), 2));
//            subquery.groupBy(roomBenefitJoin.get("id"));
//            query.multiselect(roomBenefitJoin.get("name"),
//                    roomBenefitJoin.get("id"),
//                    cb.coalesce(subquery.getSelection(), 0L).alias("countHotel"));
//            return cb.conjunction();
//        };
//    }

}
