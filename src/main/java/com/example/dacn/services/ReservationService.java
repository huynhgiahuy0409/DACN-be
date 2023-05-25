package com.example.dacn.services;

import com.example.dacn.dto.RatingDTO;
import com.example.dacn.dto.ReservationDTO;
import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.dto.response.ReservationResponse;
import com.example.dacn.model.ReservationEntity;
import com.example.dacn.model.RoomEntity;
import com.example.dacn.model.UserRating;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationResponse findById(Long id) throws Exception;

    List<Long> findReservationBefore(Long hotelId, Long roomId, LocalDate startDate, LocalDate endDate);

    List<ReservationResponse> findAllByUsername(String username);

    ReservationResponse save(ReservationRequest request) throws Exception;

    ReservationResponse cancelReservation(Long id, String username) throws Exception;

    List<ReservationResponse> saveAll(List<ReservationRequest> request);



}
