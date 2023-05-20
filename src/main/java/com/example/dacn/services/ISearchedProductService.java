package com.example.dacn.services;


import com.example.dacn.model.SearchedProductSorter;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.responsemodel.SearchedProductItemResponse;
import com.example.dacn.responsemodel.SearchedProductResponse;

import java.util.Set;

public interface ISearchedProductService {

    SearchedProductResponse getSearchedProductFromAutocomplete(ProductFilterRequest productFilterRequest);

}
