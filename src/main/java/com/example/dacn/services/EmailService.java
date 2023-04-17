package com.example.dacn.services;

import java.time.LocalDate;

public interface EmailService {
    String sendReservationMail(Long id, String to, String hotelName, String location, Double price, LocalDate startDate, LocalDate endDate);
}
