package com.example.dacn.services.impl;

import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.RoomEntity;
import com.example.dacn.repository.RoomRepository;
import com.example.dacn.services.RoomService;
import com.example.dacn.specification.RoomSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public RoomEntity getRoom(Long id) throws Exception {
        RoomEntity h = repository.findById(id).orElseThrow(() -> new Exception("Room not found" + id));
        return ResponseEntity.ok(h).getBody();

    }

    @Override
    public List<RoomEntity> getAllRoom() throws Exception {
        return repository.findAll();
    }

    @Override
    public RoomEntity createRoom(RoomEntity room) throws Exception {
        return repository.save(room);
    }

    @Override
    public ResponseEntity<RoomEntity> updateRoom(Long id, RoomEntity room) throws Exception {
        RoomEntity h = repository.findById(id).orElseThrow(() -> new Exception("Room not found" + id));
        h.setName(room.getName());
        h.setMaxAdults(room.getMaxAdults());
        h.setMaxChildren(room.getMaxChildren());
        h.setStatus(room.getStatus());
        h.setOriginPrice(room.getOriginPrice());
        h.setRentalPrice(room.getRentalPrice());
        h.setRoomType(room.getRoomType());
        h.setHotel(room.getHotel());
        h.setFacilities(room.getFacilities());
        h.setBenefits(room.getBenefits());
        h.setRoomImages(room.getRoomImages());
        h.setPaymentMethods(room.getPaymentMethods());
        h.setDiscounts(room.getDiscounts());
        h.setReservations(room.getReservations());
        h.setCartItems(room.getCartItems());
        RoomEntity re = repository.save(h);
        return ResponseEntity.ok(re);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteRoom(Long id) throws Exception {
        RoomEntity h = repository.findById(id).orElseThrow(() -> new Exception("Room not found" + id));
        repository.delete(h);
        Map<String, Boolean> re = new HashMap<>();
        re.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(re);
    }


}
