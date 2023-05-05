package com.example.dacn.dto.response;


import lombok.Data;

@Data
public class AutocompleteSearchResponse {
    private String type; //place or hotel
    private String category;
    private String name;
    private Long hotelId;
    private String searchValue;
}
