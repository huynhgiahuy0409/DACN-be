package com.example.dacn.services.impl;

import com.example.dacn.model.HotelImageEntity;
import com.example.dacn.repository.HotelImageRepository;
import com.example.dacn.services.HotelImageService;
import com.example.dacn.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelImageServiceImpl implements HotelImageService {

    @Autowired
    private HotelImageRepository hotelImageRepository;
    @Override
    public HotelImageEntity createHotelImage(HotelImageEntity image) throws Exception {
        return hotelImageRepository.save(image);
    }
}
