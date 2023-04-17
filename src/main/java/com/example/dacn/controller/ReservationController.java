package com.example.dacn.controller;

import com.example.dacn.dto.request.CancelReservationRequest;
import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.dto.response.ReservationResponse;
import com.example.dacn.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin("http://localhost:4200")
public class ReservationController {
    @Autowired
    private ReservationService service;

    @GetMapping("/getReservationById/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("id") Long id) {
        try {
            ReservationResponse reservationResponse = service.findById(id);
            return ResponseEntity.ok().body(reservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAllByUsername/{username}")
    public ResponseEntity<?> getAllByUsername(@PathVariable("username") String username) {
        try {
            List<ReservationResponse> list = service.findAllByUsername(username);
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/saveReservation")
    public ResponseEntity<?> saveReservation(@RequestBody ReservationRequest request) {
        try {
            ReservationResponse reservationResponse = service.save(request);
            return ResponseEntity.ok().body(reservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/cancelReservation")
    public ResponseEntity<?> cancelReservation(@RequestBody CancelReservationRequest request) {
        try {
            ReservationResponse reservationResponse = service.cancelReservation(request.getId(), request.getUsername());
            return ResponseEntity.ok().body(reservationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
