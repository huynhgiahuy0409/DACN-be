package com.example.dacn.responsemodel;

import com.example.dacn.dto.JWTDTO;
import com.example.dacn.dto.UserDTO;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private UserDTO userDTO;
    private JWTDTO accessToken;
    private JWTDTO refreshToken;

    public AuthenticationResponse(UserDTO userDTO, JWTDTO accessToken, JWTDTO refreshToken) {
        this.userDTO = userDTO;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
