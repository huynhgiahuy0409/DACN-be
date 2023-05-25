package com.example.dacn.specification;

import com.example.dacn.entity.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
    public static Specification<RoomEntity> findValidCheapestRoom(Long hotelId, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<RoomEntity> subRoot = subquery.from(RoomEntity.class);
            subquery.select(criteriaBuilder.min(subRoot.get("rentalPrice")))
                    .where(criteriaBuilder.and(
                            criteriaBuilder.equal(subRoot.get("hotel").get("id"), hotelId),
                            criteriaBuilder.equal(subRoot.get("maxAdults"), maxAdult),
                            criteriaBuilder.equal(subRoot.get("maxChildren"), maxChildren)
                    ));
            query.where(criteriaBuilder.equal(root.get("rentalPrice"), subquery));
            return query.getRestriction();
        };
    }
    public static Specification<RoomEntity> findValidCheapestRoom(Collection<Long> hotelIds, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<RoomEntity> subRoot = subquery.from(RoomEntity.class);
            subquery.select(criteriaBuilder.min(subRoot.get("rentalPrice")))
                    .where(criteriaBuilder.and(
                            subRoot.get("hotel").get("id").in(hotelIds),
                            criteriaBuilder.equal(subRoot.get("maxAdults"), maxAdult),
                            criteriaBuilder.equal(subRoot.get("maxChildren"), maxChildren)
                    ));
            query.where(criteriaBuilder.equal(root.get("rentalPrice"), subquery));
            return query.getRestriction();
        };
    }
    public static Specification<RoomEntity> findExpensivestRoomWithValidCapacity(Long hotelId, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<RoomEntity> subRoot = subquery.from(RoomEntity.class);
            subquery.select(criteriaBuilder.max(subRoot.get("rentalPrice")))
                    .where(criteriaBuilder.and(
                            criteriaBuilder.equal(subRoot.get("hotel").get("id"), hotelId),
                            criteriaBuilder.equal(subRoot.get("maxAdults"), maxAdult),
                            criteriaBuilder.equal(subRoot.get("maxChildren"), maxChildren)
                    ));
            query.where(criteriaBuilder.equal(root.get("rentalPrice"), subquery));
            return query.getRestriction();
        };
    }
    public static Specification<RoomEntity> findExpensivestRoomWithValidCapacity(Collection<Long> hotelIds, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
            // Subquery để tìm ra phòng có giá nhỏ nhất và thoả maxAdult
            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<RoomEntity> subRoot = subquery.from(RoomEntity.class);
            subquery.select(criteriaBuilder.max(subRoot.get("rentalPrice")));
            subquery.where(
                    criteriaBuilder.and(
                            root.get("hotel").get("id").in(hotelIds),
                            criteriaBuilder.equal(subRoot.get("maxAdults"), maxAdult),
                            criteriaBuilder.equal(subRoot.get("maxChildren"), maxChildren)
                    )
            );
            query.where(criteriaBuilder.equal(root.get("rentalPrice"), subquery));
            // Lọc ra danh sách các phòng còn lại trong cùng khách sạn với giá nhỏ hơn hoặc bằng giá của phòng tìm được và cũng thoả maxAdult
            return query.getRestriction();
        };
    }
}
