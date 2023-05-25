package com.example.dacn.specification.builder;

import com.example.dacn.entity.WardEntity;
import com.example.dacn.specification.LocationSpecification;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WardSpecificationBuilder {

    private List<SearchCriteria> params;

    public WardSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
    public WardSpecificationBuilder with(String key, Object value, String operator){
        params.add(new SearchCriteria(key, value, operator));
        return this;
    }
    public Specification<WardEntity> build() {
        if (params.size() == 0) {
            return null;
        }
        List<Specification<WardEntity>> specs = params.stream().map(LocationSpecification::new)
                .collect(Collectors.toList());
        Specification<WardEntity> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }
}
