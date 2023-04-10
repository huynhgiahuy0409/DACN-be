package com.example.dacn.services.impl;

import com.example.dacn.model.ReservationEntity;
import com.example.dacn.repository.ReservationRepository;
import com.example.dacn.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repository;

    @Override
    public ReservationEntity findById(Long id) throws Exception {
        Optional<ReservationEntity> foundReservation = repository.findById(id);
        if (!foundReservation.isPresent()) throw new Exception("No reservation was found !");
        return foundReservation.get();
    }
}
