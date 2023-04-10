package com.example.dacn.services;

import com.example.dacn.model.EmailDetails;

public interface EmailService {
    String sendReservationMail(EmailDetails details);
}
