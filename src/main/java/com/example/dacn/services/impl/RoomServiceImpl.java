package com.example.dacn.services.impl;

import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.entity.DiscountEntity;
import com.example.dacn.entity.RoomEntity;
import com.example.dacn.repository.RoomRepository;
import com.example.dacn.services.RoomService;
import com.example.dacn.specification.RoomSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public RoomEntity findByHotelAndRoomId(Long hotelId, Long roomId) {
        return repository.findAll(RoomSpecification.hasHotelAndRoomId(hotelId, roomId)).get(0);
    }

    @Override
    public RoomResponse setRoomStatus(Long hotelId, Long roomId, String status) throws Exception {
        RoomEntity foundRoom = findByHotelAndRoomId(hotelId, roomId);
        if (null == foundRoom) throw new Exception("Phòng không tồn tại");
        foundRoom.setStatus(status);
        repository.save(foundRoom);
        return mapper.map(foundRoom, RoomResponse.class);
    }

    @Override
    public RoomEntity findOne(Specification<RoomEntity> spec) {
        if(this.repository.findOne(spec).isPresent()){
            return this.repository.findOne(spec).get();
        }else{
            return null;
        }
    }

    @Override
    public List<RoomEntity> findAll(Specification<RoomEntity> spec) {
        return this.repository.findAll(spec);
    }

    @Override
    public Double computeFinalPrice(RoomEntity room) {
        double finalPrice = 0;
        DiscountEntity roomDiscount = room.getDiscount();
        Double truthPercent = 100 - roomDiscount.getDiscountPercent();
        finalPrice += (room.getRentalPrice() * (truthPercent / 100));
        return finalPrice;
    }
}
