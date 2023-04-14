package com.example.dacn.services.impl;

import com.example.dacn.dto.UserDTO;
import com.example.dacn.model.UserEntity;
import com.example.dacn.repository.IUserRepository;
import com.example.dacn.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class UserService  implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ModelMapper mp;

    @Override
    public UserDTO findByUsername(String username) {
        return this.mp.map(this.userRepository.findByUsername(username), UserDTO.class);
    }

    //    @Override
//    public boolean checkPassword(UserDetails userDetails, String password) {
//        return this.passwordEncoder.matches(password, userDetails.getPassword());
//    }
    public boolean checkPassword(UserDetails userDetails, String password) {
        return userDetails.getPassword().equals(password);
    }
}
