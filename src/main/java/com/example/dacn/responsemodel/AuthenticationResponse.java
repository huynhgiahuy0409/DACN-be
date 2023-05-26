package com.example.dacn.responsemodel;

import com.example.dacn.dto.JWTDTO;
import com.example.dacn.dto.UserDTO;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private UserDTO user;
    private JWTDTO accessToken;
    private JWTDTO refreshToken;

    public AuthenticationResponse(UserDTO user, JWTDTO accessToken, JWTDTO refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
