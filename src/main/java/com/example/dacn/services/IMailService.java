package com.example.dacn.services;

import javax.mail.MessagingException;

public interface IMailService {
    public void sendOTPMessage(String to, String subject, String message) throws MessagingException;
}
