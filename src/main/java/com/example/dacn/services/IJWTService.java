package com.example.dacn.services;

import com.example.dacn.dto.JWTDTO;
import com.example.dacn.entity.JWTEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IJWTService {

    JWTDTO save(JWTEntity token);

    JWTEntity findByToken(String token);

    Long removeByToken(String token);

    String generateToken(UserDetails userDetails, String type);

    String doGenerateToken(Map<String, Object> claims, String subject, String type);

    boolean validateToken(String authToken, UserDetails userDetails);

    String getUsernameFromToken(String token);

    Date getExpirationDateFromToken(String token);

    List<SimpleGrantedAuthority> getRolesFromToken(String token);

    Authentication getAuthentication(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);

    String getTokenFromHeader(HttpServletRequest request);
}
