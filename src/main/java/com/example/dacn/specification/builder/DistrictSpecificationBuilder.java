package com.example.dacn.specification.builder;

import com.example.dacn.entity.DistrictEntity;
import com.example.dacn.specification.DistrictSpecification;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DistrictSpecificationBuilder {
    private List<SearchCriteria> params;

    public DistrictSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
    public DistrictSpecificationBuilder with(String key, Object value, String operator){
        params.add(new SearchCriteria(key, value, operator));
        return this;
    }
    public Specification<DistrictEntity> build() {
        if (params.size() == 0) {
            return null;
        }
        List<Specification<DistrictEntity>> specs = params.stream().map(DistrictSpecification::new)
                .collect(Collectors.toList());
        Specification<DistrictEntity> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }
}
