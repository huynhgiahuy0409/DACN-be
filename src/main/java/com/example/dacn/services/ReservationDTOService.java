package com.example.dacn.services;

import com.example.dacn.dto.ReservationDTO;
import com.example.dacn.model.ReservationEntity;

import java.util.List;

public interface ReservationDTOService {
    ReservationDTO getReservationDTO(ReservationEntity u) throws Exception;
    List<ReservationDTO> getReservationDTO() throws Exception;
}
