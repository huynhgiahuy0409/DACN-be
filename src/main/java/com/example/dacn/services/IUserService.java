package com.example.dacn.services;

import com.example.dacn.dto.UserDTO;
import com.example.dacn.model.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUserService {

    public UserDTO findByUsername(String username);
    public boolean checkPassword(UserDetails userDetails, String password);
}
