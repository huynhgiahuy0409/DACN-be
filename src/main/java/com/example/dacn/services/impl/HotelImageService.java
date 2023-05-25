package com.example.dacn.services.impl;

import com.example.dacn.entity.HotelImageEntity;
import com.example.dacn.repository.IHotelImageRepository;
import com.example.dacn.services.IHotelImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelImageService implements IHotelImageService {
    @Autowired
    private IHotelImageRepository hotelImageRepository;

    @Override
    public String findFirstBannerImage(Long hotelId) {
        HotelImageEntity image = hotelImageRepository.findFirstByIsThumbnailIsTrueAndHotelId(hotelId);
        if (null == image) return "";
        return image.getUrl();
    }
}
