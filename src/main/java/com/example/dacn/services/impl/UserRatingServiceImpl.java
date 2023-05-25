package com.example.dacn.services.impl;

import com.example.dacn.dto.RatingDTO;


import com.example.dacn.entity.UserRating;
import com.example.dacn.repository.UserRatingRepository;
import com.example.dacn.services.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRatingServiceImpl implements UserRatingService {
    @Autowired
    private UserRatingRepository repository;


    @Override
    public RatingDTO getRatingDto(UserRating u) throws Exception {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setId(u.getId());
        ratingDTO.setContent(u.getContent());
        ratingDTO.setTitle(u.getTitle());
        ratingDTO.setPoints(u.getPoints());
        ratingDTO.setUpdateDate(u.getUpdateDate());
        ratingDTO.setUsername(u.getUser().getUsername());
        ratingDTO.setHotelId(u.getHotel().getId());
        return ratingDTO;
    }

    @Override
    public List<RatingDTO> getAllRating() throws Exception {

        List<UserRating> allUserRatings = repository.findAll();
        List<RatingDTO> allRatingDto = new ArrayList<>();
        for (UserRating p : allUserRatings) {
            allRatingDto.add(getRatingDto(p));
        }
        return allRatingDto;
    }
}
