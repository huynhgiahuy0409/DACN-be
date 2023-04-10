package com.example.dacn.services;

import com.example.dacn.model.ReservationEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {
    ReservationEntity findById(Long id) throws Exception;
}
