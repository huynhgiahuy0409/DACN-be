package com.example.dacn.services.impl;

import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.enums.ReservationStatus;
import com.example.dacn.model.BaseEntity;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.ReservationEntity;
import com.example.dacn.repository.ReservationRepository;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.ReservationService;
import com.example.dacn.services.RoomService;
import com.example.dacn.specification.ReservationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;

    @Override
    public ReservationEntity findById(Long id) throws Exception {
        Optional<ReservationEntity> foundReservation = repository.findById(id);
        if (!foundReservation.isPresent()) throw new Exception("No reservation was found !");
        return foundReservation.get();
    }

    @Override
    public List<Long> findReservationBefore(Long hotelId, Long roomId, LocalDate startDate, LocalDate endDate) {
        return repository.findAll(ReservationSpecification.hasReserveBefore(hotelId, roomId, startDate, endDate))
                .stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<ReservationEntity> findALl() {
        return repository.findAll();
    }

    @Override
    public ReservationEntity save(ReservationRequest request) throws Exception {
        HotelEntity hotel = hotelService.findById(request.getHotelId());
        ReservationEntity r = ReservationEntity.builder()
                .adult(request.getAdult())
                .discountPercent(request.getDiscountPercent())
                .children(request.getChildren())
                .endDate(request.getEndDate())
                .startDate(request.getStartDate())
                .hotel(hotel)
                .room(roomService.findByHotelAndRoomId(request.getHotelId(), request.getRoomId()))
                .status(ReservationStatus.PENDING)
                .build();
        return repository.save(r);
    }
}
