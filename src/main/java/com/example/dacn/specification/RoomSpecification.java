package com.example.dacn.specification;

import com.example.dacn.entity.RoomEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
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
    public static Specification<RoomEntity> validRoomCapacity(String param, Integer value){
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(param), value.toString());
    }

    public static Specification<RoomEntity> findCheapestRoomWithValidCapacity(Long hotelId, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
            // Subquery để tìm ra phòng có giá nhỏ nhất và thoả maxAdult
            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<RoomEntity> subRoot = subquery.from(RoomEntity.class);
            subquery.select(criteriaBuilder.min(subRoot.get("rentalPrice")));
            subquery.where(
                    criteriaBuilder.and(
                            criteriaBuilder.equal(subRoot.get("hotel").get("id"), hotelId),
                            criteriaBuilder.equal(subRoot.get("maxAdults"), maxAdult),
                            criteriaBuilder.equal(subRoot.get("maxChildren"), maxChildren)
                    )
            );
            // Lọc ra danh sách các phòng còn lại trong cùng khách sạn với giá nhỏ hơn hoặc bằng giá của phòng tìm được và cũng thoả maxAdult
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("hotel").get("id"), hotelId),
                    criteriaBuilder.lessThanOrEqualTo(root.get("rentalPrice"), criteriaBuilder.<BigDecimal>coalesce(subquery, root.get("rentalPrice"))),
                    criteriaBuilder.equal(root.get("maxAdults"), maxAdult),
                    criteriaBuilder.equal(root.get("maxChildren"), maxChildren)
            );
        };
    }
}
