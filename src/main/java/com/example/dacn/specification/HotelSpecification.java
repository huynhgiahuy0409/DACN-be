package com.example.dacn.specification;

import com.example.dacn.model.*;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

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
        }else if(operator.equals("equals")){
            return criteriaBuilder.like(root.get(param), value.toString());
        }else if(operator.equals("province-like")){
            Join<HotelEntity, AddressEntity> addressJoin = root.join("address");
            Join<AddressEntity, ProvinceEntity> provinceJoin =  addressJoin.join("province");
            return criteriaBuilder.like(provinceJoin.get(param), "%" + value.toString() + "%");
        }
        return null;
    }
}
