package com.example.dacn.specification;

import com.example.dacn.model.*;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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
            }else if(property.equals("product-name")){
                System.out.println(property);
                orders.add(criteriaBuilder.desc(root.get("name")));
            }
            query.orderBy(orders);
            return criteriaBuilder.conjunction();
        };
    }
}
