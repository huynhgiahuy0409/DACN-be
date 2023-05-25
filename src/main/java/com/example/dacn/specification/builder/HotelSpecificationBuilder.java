package com.example.dacn.specification.builder;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.specification.HotelSpecification;
import com.example.dacn.specification.criteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HotelSpecificationBuilder {

    private List<SearchCriteria> params;

    public HotelSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }
    public HotelSpecificationBuilder with(String key, Object value, String operator){
        params.add(new SearchCriteria(key, value, operator));
        return this;
    }
    public Specification<HotelEntity> build() {
        if (params.size() == 0) {
            return null;
        }
        List<Specification<HotelEntity>> specs = params.stream().map(HotelSpecification::new)
                .collect(Collectors.toList());
        Specification<HotelEntity> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }


}
