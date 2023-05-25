package com.example.dacn.services.impl;

import com.example.dacn.dto.RatingDTO;
import com.example.dacn.dto.ReservationDTO;

import com.example.dacn.entity.ReservationEntity;
import com.example.dacn.repository.ReservationRepository;
import com.example.dacn.services.ReservationDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ReservationDTOServiceImpl implements ReservationDTOService {
    @Autowired
    private ReservationRepository repository;
    @Override
    public List<ReservationDTO> getReservationDTO() throws Exception {

        List<ReservationEntity> allReservations = repository.findAll();
        List<ReservationDTO> allReservationDto = new ArrayList<>();
        for (ReservationEntity p : allReservations) {
            allReservationDto.add(getReservationDTO(p));
        }
        return allReservationDto;
    }

    @Override
    public ReservationDTO getReservationDTO(ReservationEntity u) throws Exception {
        ReservationDTO reDTO = new ReservationDTO();
        reDTO.setId(u.getId());
        reDTO.setCreatedDate(u.getCreatedDate());
        reDTO.setModifiedDate(u.getModifiedDate());
        reDTO.setAdult(u.getAdult());
        reDTO.setChildren(u.getChildren());
        reDTO.setDiscountPercent(u.getDiscountPercent());
        reDTO.setPrice( u.getPrice());
        reDTO.setStatus(u.getStatus());
        reDTO.setHotelId(u.getHotel().getId());
        reDTO.setRoomId(u.getRoom().getId());
        reDTO.setUsername(u.getUser().getUsername());

        return  reDTO;
    }

}
