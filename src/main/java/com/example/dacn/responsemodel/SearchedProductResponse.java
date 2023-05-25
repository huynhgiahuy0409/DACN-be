package com.example.dacn.responsemodel;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SearchedProductResponse {
    private SearchedProductItemResponse searchedProduct;

    private List<SearchedProductItemResponse> relativeSearchedProducts;

    private Double minFinalPrice;

    private Double maxFinalPrice;

}
