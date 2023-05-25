package com.example.dacn.services;

import com.example.dacn.dto.response.AutocompleteSearchResponse;

import java.util.List;
import java.util.Set;

public interface IAutocompleteService {
    List<AutocompleteSearchResponse> buildAnAutocompletes(String searchedValue);
}
