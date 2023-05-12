package com.example.dacn.controller;

import com.example.dacn.model.HotelEntity;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.impl.HotelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin("http://localhost:4200")
public class HotelController {

    @Autowired
    private HotelServiceImpl hotelService;

    @GetMapping("/hotel_profiles")
    public List<HotelEntity> getAllHotelEntitys() throws Exception {
        return hotelService.getAllHotel();

    }


    @PostMapping("/hotel_profile")
    public HotelEntity createHotelEntity(@RequestBody HotelEntity hp) throws Exception {
        return hotelService.createHotel(hp);
    }

    @GetMapping("/hotel_profiles/{id}")
    public HotelEntity getHotelEntity(@PathVariable Long id) throws Exception {
        return hotelService.getHotel(id);
    }

    @PutMapping("/hotel_profiles/{id}")
    public ResponseEntity<HotelEntity> updateHotelEntity(@PathVariable Long id, @RequestBody HotelEntity hotelEntity) throws Exception {
        return hotelService.updateHotel(id, hotelEntity);

    }

    @DeleteMapping("/hotel_profiles/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteHotelEntity(@PathVariable Long id) throws Exception {
        return hotelService.deleteHotel(id);
    }
}
