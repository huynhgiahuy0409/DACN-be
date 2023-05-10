package com.example.dacn.specification;

import com.example.dacn.entity.WardEntity;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class LocationSpecification implements Specification<WardEntity> {
    private SearchCriteria criteria;

    public LocationSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(Root<WardEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        String param = this.criteria.getKey();
        Object value = this.criteria.getValue();
        String operator = this.criteria.getOperator();
//        if (operator.equals("room-capacity is gt|et")) {
//
//            return criteriaBuilder.greaterThanOrEqualTo();
//        }

        return null;
    }
}
