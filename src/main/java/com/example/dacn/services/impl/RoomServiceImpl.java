package com.example.dacn.services.impl;

import com.example.dacn.dto.response.RoomResponse;

import com.example.dacn.entity.DiscountEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.entity.RoomEntity;
import com.example.dacn.repository.HotelRepository;

import com.example.dacn.repository.RoomRepository;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.requestmodel.ProductSortRequest;
import com.example.dacn.services.RoomService;
import com.example.dacn.specification.RoomSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private HotelRepository hotelRepository;

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
        h.setDiscount(room.getDiscount());
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



    public RoomEntity findOne(Specification<RoomEntity> spec) {
        if (this.repository.findOne(spec).isPresent()) {
            return this.repository.findOne(spec).get();
        } else {
            return null;
        }
    }

    @Override
    public RoomEntity getOne(Long id) {
        return this.repository.getOne(id);
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

    @Override
    public RoomEntity findMinimumPriceRoom(Long hotelId) {
        return repository.findFirstByHotelIdOrderByFinalPriceAsc(hotelId);
    }
    public Double minPriceByFilter(ProductFilterRequest productFilterRequest) {
        Long value;
        if(productFilterRequest.getType().equals("hotel")){
            Long hotelId = productFilterRequest.getValue();
            HotelEntity searchedHotel = this.hotelRepository.getOne(hotelId);
            ProvinceEntity province = searchedHotel.getAddress().getProvince();
            value = province.getId();
        }else{
            value = productFilterRequest.getValue();
        }
        return this.repository.findMinPriceByFilter(value, productFilterRequest.getAdults(), productFilterRequest.getChildren());
    }

    @Override
    public Double maxPriceByFilter(ProductFilterRequest productFilterRequest) {
        Long value;
        if(productFilterRequest.getType().equals("hotel")){
            Long hotelId = productFilterRequest.getValue();
            HotelEntity searchedHotel = this.hotelRepository.getOne(hotelId);
            ProvinceEntity province = searchedHotel.getAddress().getProvince();
            value = province.getId();
        }else{
            value = productFilterRequest.getValue();
        }

        return this.repository.findMaxPriceByFilter(value, productFilterRequest.getAdults(), productFilterRequest.getChildren());
    }

    @Override
    public List<Long> findAllValidRoom(ProductFilterRequest productFilterRequest, Pageable pageable) {
        return this.repository.findAllValidRoom(productFilterRequest.getValue(), productFilterRequest.getAdults(), productFilterRequest.getChildren(),pageable);
    }

}
