package com.example.dacn.specification;

import com.example.dacn.entity.*;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HotelSpecification implements Specification<HotelEntity> {

    private SearchCriteria criteria;

    public HotelSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<HotelEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        String param = this.criteria.getKey();
        Object value = this.criteria.getValue();
        String operator = this.criteria.getOperator();
        if (operator.equals("like")) {
            return criteriaBuilder.like(root.get(param), "%" + value.toString() + "%");
        } else if (operator.equals("equals")) {
            return criteriaBuilder.equal(root.get(param), value.toString());
        } else if (operator.equals("province-like")) {
            Join<HotelEntity, AddressEntity> addressJoin = root.join("address");
            Join<AddressEntity, ProvinceEntity> provinceJoin = addressJoin.join("province");
            return criteriaBuilder.like(provinceJoin.get(param), "%" + value.toString() + "%");
        } else if (operator.equals("valid room capacity")) {
            Join<HotelEntity, RoomEntity> hotelRoomJoin = root.join("rooms");
            return criteriaBuilder.greaterThanOrEqualTo(hotelRoomJoin.get(param), value.toString());
        }
        return null;
    }

    public static Specification<HotelEntity> sortByAscProperty(String property) {
        return (root, query, criteriaBuilder) -> {
            List<Order> orders = new ArrayList<>();
            if (property.equals("price")) {
                Join<HotelEntity, RoomEntity> hotelRoomJoin = root.join("rooms");
                orders.add(criteriaBuilder.asc(hotelRoomJoin.get("rentalPrice")));
            } else if (property.equals("product-name")) {
                System.out.println(property);
                orders.add(criteriaBuilder.desc(root.get("name")));
            }
            query.orderBy(orders);
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<HotelEntity> findByProvince(Long provinceId) {
        return (root, query, criteriaBuilder) -> {
            Join<HotelEntity, AddressEntity> hotelAddressJoin = root.join("address");
            Join<AddressEntity, ProvinceEntity> addressProvinceJoin = hotelAddressJoin.join("province");
            return criteriaBuilder.equal(addressProvinceJoin.get("id"), provinceId.toString());
        };
    }

    public static Specification<HotelEntity> findValidSearchedHotel(Long hotelId, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
            Join<HotelEntity, RoomEntity> roomJoin = root.join("rooms");
            query.where(root.get("id").in(hotelId), criteriaBuilder.equal(roomJoin.get("maxAdults"), maxAdult), criteriaBuilder.equal(roomJoin.get("maxChildren"), maxChildren));
            return query.getRestriction();
        };
    }
    public static Specification<HotelEntity> findValidSearchedHotel(Collection<Long> hotelIds, Integer maxAdult, Integer maxChildren) {
        return (root, query, criteriaBuilder) -> {
                Join<HotelEntity, RoomEntity> roomJoin = root.join("rooms");
                query.multiselect(root, criteriaBuilder.min(roomJoin.get("rentalPrice")))
                        .where(root.get("id").in(hotelIds), criteriaBuilder.greaterThanOrEqualTo(roomJoin.get("maxAdults"), maxAdult), criteriaBuilder.equal(roomJoin.get("maxChildren"), maxChildren))
                        .groupBy(root.get("id"));
                return query.getRestriction();
        };
    }
}
