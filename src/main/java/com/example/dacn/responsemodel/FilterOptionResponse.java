package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.List;

@Data
public class FilterOptionResponse {
    List<FilterOptionItemResponse> options;
    String type;

    public FilterOptionResponse(List<FilterOptionItemResponse> options, String type) {
        this.options = options;
        this.type = type;
    }
}
