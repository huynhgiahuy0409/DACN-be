package com.example.dacn.services.impl;

import com.example.dacn.constance.SystemConstance;
import com.example.dacn.dto.JWTDTO;
import com.example.dacn.entity.JWTEntity;
import com.example.dacn.repository.IJWTRepository;
import com.example.dacn.services.IJWTService;
import com.nimbusds.jwt.JWT;
import io.jsonwebtoken.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class JWTService implements IJWTService {

    @Autowired
    private IJWTRepository jwtRepository;

    @Autowired
    private ModelMapper mp;

    @Override
    public JWTEntity save(JWTEntity jwt) {
        return jwtRepository.save(jwt);
    }

    @Override
    public JWTEntity findByToken(String token) {
        return this.jwtRepository.findByToken(token);
    }

    @Override
    public Long removeByToken(String token) {
        return this.jwtRepository.removeByToken(token);
    }

    @Override
    public String generateToken(UserDetails userDetails, String type) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails.getAuthorities() != null) {
            Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
            List<String> rolesList = new ArrayList<>();
            for (GrantedAuthority role : roles) {
                rolesList.add(role.getAuthority());
            }
            claims.put("roles", rolesList);
        }
        return doGenerateToken(claims, userDetails.getUsername(), type);
    }

    @Override
    public String doGenerateToken(Map<String, Object> claims, String subject, String type) {
        Random random = new Random();
        int randomInt = random.nextInt(10000);
        long randomLong = (long) randomInt;
        if (type == "access") {
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + SystemConstance.EXPIRATION_TIME + randomLong))
                    .signWith(SignatureAlgorithm.HS512, SystemConstance.SECRET_KEY).compact();
        } else {
            // refresh
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + SystemConstance.REFRESH_TIME + randomLong))
                    .signWith(SignatureAlgorithm.HS512, SystemConstance.SECRET_KEY).compact();
        }
    }

    @Override
    public boolean validateToken(String token) throws ExpiredJwtException {
        JWTEntity foundJwtEntity = this.jwtRepository.findByToken(token);
        if (foundJwtEntity != null && foundJwtEntity.getToken().equals(token)) {
            Jwts.parser()
                    .setSigningKey(SystemConstance.SECRET_KEY)
                    .setAllowedClockSkewSeconds(60)
                    .parseClaimsJws(token);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SystemConstance.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SystemConstance.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        return null;
    }

    // Check if JWT token is expired
    @Override
    public boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(SystemConstance.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }

    @Override
    public void parseGoogleJwt(String jwt) {
        System.out.println(jwt);

        try {
            Claims claims = Jwts.parser().parseClaimsJwt(jwt).getBody();
            System.out.println(claims);
            String subject = claims.getSubject();
            System.out.println(subject);
            // Get other claims as needed
        } catch (JwtException ex) {
            // Handle exception
            System.out.println(ex);
        }
    }

    @Override
    public JWTEntity createToken(UserDetails userDetails, String type) {
        String token = this.generateToken(userDetails, type);
        Date accessTime = this.getExpirationDateFromToken(token);
        JWTEntity savedToken = this.save(new JWTEntity(token, accessTime));
        return savedToken;
    }
}
