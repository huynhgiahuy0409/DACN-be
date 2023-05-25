package com.example.dacn.dto.response;


import lombok.Data;

@Data
public class AutocompleteSearchResponse {
    private String name;
    private String category;
    private Long value;
    private String type;

    public AutocompleteSearchResponse(String name, String category, Long value, String type) {
        this.name = name;
        this.category = category;
        this.value = value;
        this.type = type;
    }
}
