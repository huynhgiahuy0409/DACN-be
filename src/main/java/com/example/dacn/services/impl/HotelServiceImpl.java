package com.example.dacn.services.impl;

import com.example.dacn.model.HotelEntity;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository repository;

    @Override
    public HotelEntity findById(Long id) throws Exception {
        Optional<HotelEntity> hotel = repository.findById(id);
        if (!hotel.isPresent()) throw new Exception("Khách sạn không tồn tại !");
        return hotel.get();
    }

    @Override
    public HotelEntity getHotel(Long id) throws Exception {
        HotelEntity h = repository.findById(id).orElseThrow(()-> new Exception("Hotel not found"+id));
return ResponseEntity.ok(h).getBody();

    }

    @Override
    public List<HotelEntity> getAllHotel() throws Exception {
        return repository.findAll();
    }

    @Override
    public HotelEntity createHotel(HotelEntity hotel) throws Exception {
        return repository.save(hotel);
    }

    @Override
    public ResponseEntity<HotelEntity> updateHotel(Long id, HotelEntity hotel) throws Exception {
        HotelEntity h = repository.findById(id).orElseThrow(()-> new Exception("Hotel not found"+id));
        h.setHotelImages(hotel.getHotelImages());
        h.setName(hotel.getName());
        h.setAddress(hotel.getAddress());

        h.setDescription(hotel.getDescription());
        h.setOwner(hotel.getOwner());
        h.setFacilities(hotel.getFacilities());
        h.setRatings(hotel.getRatings());
        h.setAveragePoints(hotel.getAveragePoints());
        h.setStatus(hotel.getStatus());
        h.setRooms(hotel.getRooms());
        h.setReservations(hotel.getReservations());
        h.setCartItems(hotel.getCartItems());
        HotelEntity re = repository.save(h);
        return ResponseEntity.ok(re);
    }

    @Override
    public ResponseEntity<Map<String,Boolean>> deleteHotel(Long id) throws Exception {
        HotelEntity h = repository.findById(id).orElseThrow(()-> new Exception("Hotel not found"+id));
            repository.delete(h);
        Map<String, Boolean> re = new HashMap<>();
        re.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(re);
    }

}
