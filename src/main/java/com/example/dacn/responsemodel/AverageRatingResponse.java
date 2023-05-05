package com.example.dacn.responsemodel;

import lombok.Data;

@Data
public class AverageRatingResponse {
    private String name;
    private Double points;
    private Integer reviews;
}
