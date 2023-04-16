package com.example.dacn.controller;

import com.example.dacn.model.EmailDetails;
import com.example.dacn.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//test only
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details) {
        String status
                = emailService.sendReservationMail(details);
        return status;
    }
}
