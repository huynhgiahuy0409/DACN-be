package com.example.dacn.controller;

import com.example.dacn.entity.HotelImageEntity;
import com.example.dacn.services.impl.HotelImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel_image")
@CrossOrigin("http://localhost:4200")
public class HotelImageController {
    @Autowired
    private HotelImageServiceImpl hotelImageService;

    @PostMapping("/hotel_image")
    public HotelImageEntity createAddress(@RequestBody HotelImageEntity hp) throws Exception {
        return hotelImageService.createHotelImage(hp);
    }
}
