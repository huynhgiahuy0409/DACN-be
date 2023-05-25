package com.example.dacn.responsemodel;

import lombok.Data;

@Data
public class FilterOptionItemResponse {
    Object value;
    String name;
    Long total;

    public FilterOptionItemResponse(Object value, String name, Long total) {
        this.value = value;
        this.name = name;
        this.total = total;
    }

    public FilterOptionItemResponse() {
    }
}
