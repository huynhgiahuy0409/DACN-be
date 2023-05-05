package com.example.dacn.requestmodel;

import lombok.Data;

import java.util.List;

@Data
public class ProductSortRequest {
    String direction;
    String property;
}
