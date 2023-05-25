package com.example.dacn.controller;

import com.example.dacn.dto.RatingDTO;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.UserRating;
import com.example.dacn.services.impl.HotelServiceImpl;
import com.example.dacn.services.impl.UserRatingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-rating")
@CrossOrigin("http://localhost:4200")
public class UserRatingController {
    @Autowired
    private UserRatingServiceImpl ratingService;

    @GetMapping("/user-ratings")
    public List<RatingDTO> getAllUserRatings() throws Exception {
        return ratingService.getAllRating();

    }



}
