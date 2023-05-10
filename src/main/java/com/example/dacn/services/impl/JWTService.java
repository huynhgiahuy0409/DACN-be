package com.example.dacn.services.impl;

import com.example.dacn.constance.SystemConstance;
import com.example.dacn.dto.JWTDTO;
import com.example.dacn.entity.JWTEntity;
import com.example.dacn.repository.IJWTRepository;
import com.example.dacn.services.IJWTService;
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
public class JWTService  implements IJWTService {

    @Autowired
    private IJWTRepository jwtRepository;

    @Autowired
    private ModelMapper mp;

    @Override
    public JWTDTO save(JWTEntity jwt) {
        JWTEntity savedJWT = jwtRepository.save(jwt);
        return this.mp.map(savedJWT, JWTDTO.class);
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
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        List<String> rolesList = new ArrayList<>();
        for (GrantedAuthority role : roles) {
            rolesList.add(role.getAuthority());
        }
        claims.put("roles", rolesList);
        return doGenerateToken(claims, userDetails.getUsername(), type);
    }

    @Override
    public String doGenerateToken(Map<String, Object> claims, String subject, String type) {
        if(type == "access"){
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + SystemConstance.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SystemConstance.SECRET_KEY).compact();
        }else{
            // refresh
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + SystemConstance.REFRESH_TIME))
                    .signWith(SignatureAlgorithm.HS512, SystemConstance.SECRET_KEY).compact();
        }
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
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

    // Get JWT token from request header
    @Override
    public String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    // Check if JWT token is expired
    @Override
    public Authentication getAuthentication(String token, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        if (validateToken(token, userDetails)) {
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        }
        return usernamePasswordAuthenticationToken;
    }
    @Override
    public boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser()
                .setSigningKey(SystemConstance.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }
}
