package com.example.dacn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-dev.properties")
public class SecretProperties {
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.username}")
    private String email;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.properties.mail.smtp.starttls.enable}")
    private String startTls;
    @Value("${mail.properties.mail.smtp.auth}")
    private String auth;

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStartTls() {
        return startTls;
    }

    public String getAuth() {
        return auth;
    }
}
