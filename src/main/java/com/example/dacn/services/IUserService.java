package com.example.dacn.services;

import com.example.dacn.dto.GuestDTO;
import com.example.dacn.dto.UserDTO;
import com.example.dacn.entity.UserEntity;
import com.example.dacn.enums.UserStatus;
import com.example.dacn.requestmodel.SignUpRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {

    UserDTO findByUsernameDTO(String username);

    UserEntity findByUsername(String username);

    boolean checkPassword(UserDetails userDetails, String password);

    boolean checkValidPassword(String password);

    boolean checkExistUser(String username);

    UserDTO save(UserEntity user);

    UserEntity findByUsernameOrEmail(String username, String email);

    UserEntity saveGuest(GuestDTO guest);

    UserEntity generateUser(SignUpRequest req, UserStatus status);

    UserDTO getUserDTO(String username);
    UserDTO getUserDTO(String username, String socialAvatarUrl);
}
