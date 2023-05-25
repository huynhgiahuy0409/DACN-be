package com.example.dacn.specification;

import com.example.dacn.entity.CartEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class CartSpecification {
    public static Specification<CartEntity> hasSessionId(String sessionId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sessionId"), sessionId);
    }

    public static Specification<CartEntity> hasHotelRoomSessionId(Long roomId, String sessionId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
//            predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
            predicates.add(cb.equal(root.get("room").get("id"), roomId));
            predicates.add(cb.equal(root.get("sessionId"), sessionId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
