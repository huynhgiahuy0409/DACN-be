package com.example.dacn.services;

import com.example.dacn.dto.GuestDTO;
import com.example.dacn.dto.UserDTO;
import com.example.dacn.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {

    UserDTO findByUsernameDTO(String username);

    UserEntity findByUsername(String username);

    boolean checkPassword(UserDetails userDetails, String password);

    boolean checkValidPassword(String password);

    boolean checkExistUsername(String username);

    UserDTO save(UserEntity user);

    UserEntity findByUsernameOrEmail(String username, String email);

    UserEntity saveGuest(GuestDTO guest);
}
