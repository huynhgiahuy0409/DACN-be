package com.example.dacn.services;

import com.example.dacn.requestmodel.SaveFavoriteHotelRequest;
import com.example.dacn.responsemodel.FavoriteHotelResponse;

import java.util.List;

public interface IFavoriteHotelService {
    List<FavoriteHotelResponse> findAllByUsername(String username);
    FavoriteHotelResponse save(SaveFavoriteHotelRequest request) throws Exception;
    Long delete(Long id) throws Exception;
}
