package com.example.dacn.specification;

import com.example.dacn.model.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {
    public static Specification<RoomEntity> hasHotelAndRoomId(Long hotelId, Long roomId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
            predicates.add(cb.equal(root.get("id"), roomId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<RoomEntity> getCheapestRoomByHotelId(Long hotelId) {
        return (root, query, cb) -> {
            // Tạo một subquery để lấy ra giá thấp nhất của khách sạn
            Subquery<Double> cheapestPriceSubquery = query.subquery(Double.class);
            Root<RoomEntity> roomRoot = cheapestPriceSubquery.from(RoomEntity.class);
            cheapestPriceSubquery.select(cb.min(roomRoot.get("rentalPrice")));
            cheapestPriceSubquery.where(cb.equal(roomRoot.get("hotel").get("id"), hotelId));

            // Lấy ra phòng có giá bằng giá thấp nhất của khách sạn
            query.orderBy(cb.asc(root.get("rentalPrice")));
            return cb.and(
                    cb.equal(root.get("hotel").get("id"), hotelId),
                    cb.equal(root.get("rentalPrice"), cheapestPriceSubquery)
            );
        };
    }
}
