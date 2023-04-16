package com.example.dacn.services.impl;

import com.example.dacn.config.SecretProperties;
import com.example.dacn.model.EmailDetails;
import com.example.dacn.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service

public class EmailServiceImpl implements EmailService {
    private String host;
    private String port;
    private String email;
    private String password;
    private String startTls;
    private String auth;
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";

    @Autowired
    ReservationMailTemplateService reservationMailService;
    @Autowired
    private SecretProperties secretProperties;

    @PostConstruct
    public void init() {
        host = secretProperties.getHost();
        port = secretProperties.getPort();
        email = secretProperties.getEmail();
        password = secretProperties.getPassword();
        startTls = secretProperties.getStartTls();
        auth = secretProperties.getAuth();
    }

    public String sendReservationMail(EmailDetails details) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.starttls.enable", startTls);
            props.put("mail.smtp.auth", auth);
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
