package com.example.dacn.services;


import com.example.dacn.model.SearchedProductSorter;
import com.example.dacn.responsemodel.SearchedProductItemResponse;
import com.example.dacn.responsemodel.SearchedProductResponse;

import java.util.Set;

public interface ISearchedProductService {

    SearchedProductResponse getSearchedProductFromAutocomplete(Long id, String type, Integer maxAdult, Integer maxChildren, SearchedProductSorter sorter);
}
