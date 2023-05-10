package com.example.dacn.specification;

import com.example.dacn.entity.ReservationEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationSpecification {
    public static Specification<ReservationEntity> hasReserveBefore(Long hotelId, Long roomId, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
            predicates.add(cb.equal(root.get("room").get("id"), roomId));
            predicates.add(
                    cb.or(
                            cb.or(cb.greaterThanOrEqualTo(root.get("startDate"), startDate)),
                            cb.or(cb.greaterThanOrEqualTo(root.get("endDate"), startDate)),
                            cb.or(cb.greaterThanOrEqualTo(root.get("endDate"), endDate)),
                            cb.or(cb.greaterThanOrEqualTo(root.get("startDate"), endDate))
                    ));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ReservationEntity> hasUsername(String username) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("user").get("username"), username));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
