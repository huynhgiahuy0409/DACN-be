package com.example.dacn.specification;

import com.example.dacn.model.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
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
}
