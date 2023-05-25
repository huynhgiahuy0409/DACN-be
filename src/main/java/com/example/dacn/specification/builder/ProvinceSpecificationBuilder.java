package com.example.dacn.specification.builder;

import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.specification.ProvinceSpecification;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProvinceSpecificationBuilder {
    private List<SearchCriteria> params;

    public ProvinceSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
    public ProvinceSpecificationBuilder with(String key, Object value, String operator){
        params.add(new SearchCriteria(key, value, operator));
        return this;
    }
    public Specification<ProvinceEntity> build() {
        if (params.size() == 0) {
            return null;
        }
        List<Specification<ProvinceEntity>> specs = params.stream().map(ProvinceSpecification::new)
                .collect(Collectors.toList());
        Specification<ProvinceEntity> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }
}
