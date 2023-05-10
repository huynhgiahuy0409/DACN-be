package com.example.dacn.model;

import lombok.Data;

@Data
public class SearchedProductSorter {
    private String property;
    private String direction;

    public SearchedProductSorter(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }

    public SearchedProductSorter() {
    }
}
