package com.example.dacn.services.impl;

import com.example.dacn.model.EmailDetails;
import com.example.dacn.services.EmailService;
import com.example.dacn.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service

public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String email;
    @Value("${spring.mail.password}")
    private String password;
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Autowired
    ReservationMailTemplateService reservationMailService;

    public String sendReservationMail(EmailDetails details) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", port);
            Session session = Session.getInstance(props,
                    new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(email, password);
                        }
                    });
            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(email));
            mailMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(details.getRecipient())});
            mailMessage.setContent(reservationMailService.getReservationContent(), CONTENT_TYPE_TEXT_HTML);
            mailMessage.setSubject(details.getSubject());
            Transport.send(mailMessage);
            return "Email was sent !";
        } catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
}
