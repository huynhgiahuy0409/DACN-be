package com.example.dacn.services;

import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.model.ReservationEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservationService {
    ReservationEntity findById(Long id) throws Exception;

    List<Long> findReservationBefore(Long hotelId, Long roomId, LocalDate startDate, LocalDate endDate);

    List<ReservationEntity> findALl();

    ReservationEntity save(ReservationRequest request) throws Exception;
}
