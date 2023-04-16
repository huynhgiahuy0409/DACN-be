package com.example.dacn.services;

import com.example.dacn.model.EmailDetails;

import javax.mail.MessagingException;

public interface EmailService {
    String sendReservationMail(EmailDetails details);
}
