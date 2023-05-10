package com.example.dacn.specification;

import com.example.dacn.entity.DistrictEntity;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DistrictSpecification implements Specification<DistrictEntity> {
    private SearchCriteria criteria;

    public DistrictSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }
    @Override
    public Predicate toPredicate(Root<DistrictEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        String param = this.criteria.getKey();
        Object value = this.criteria.getValue();
        String operator = this.criteria.getOperator();
        if (operator.equals("like")) {
            return criteriaBuilder.like(root.get(param), "%" + value.toString() + "%");
        } else if (operator.equals("equals")) {
            return criteriaBuilder.equal(root.get(param), value.toString());
        }
        return null;
    }
}
