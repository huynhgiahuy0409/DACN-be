package com.example.dacn.specification;

import com.example.dacn.entity.ReservationEntity;
import com.example.dacn.enums.ReservationStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationSpecification {
    public static Specification<ReservationEntity> hasReserveBefore(Long roomId, LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("room").get("id"), roomId));
            predicates.add(
                    cb.or(
                            cb.greaterThanOrEqualTo(root.get("startDate"), startDate),
                            cb.greaterThanOrEqualTo(root.get("endDate"), startDate),
                            cb.greaterThanOrEqualTo(root.get("endDate"), endDate),
                            cb.greaterThanOrEqualTo(root.get("startDate"), endDate)
                    ));
            predicates.add(cb.between(root.get("status"), ReservationStatus.PENDING, ReservationStatus.ACCEPTED));
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
