package com.example.dacn.services;

import com.example.dacn.dto.RatingDTO;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.UserRating;

import java.util.List;

public interface UserRatingService {



    RatingDTO getRatingDto(UserRating u) throws Exception;
    List<RatingDTO> getAllRating() throws Exception;

}
