package com.example.dacn.services.impl;

import com.example.dacn.model.HotelEntity;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository repository;

    @Override
    public HotelEntity findById(Long id) throws Exception {
        Optional<HotelEntity> hotel = repository.findById(id);
        if (!hotel.isPresent()) throw new Exception("Khách sạn không tồn tại !");
        return hotel.get();
    }
}
