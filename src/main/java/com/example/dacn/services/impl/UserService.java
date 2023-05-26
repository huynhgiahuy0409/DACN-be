package com.example.dacn.services.impl;

import com.example.dacn.dto.GuestDTO;
import com.example.dacn.dto.UserDTO;
import com.example.dacn.entity.UserEntity;
import com.example.dacn.repository.IUserRepository;
import com.example.dacn.services.IUserService;
import com.example.dacn.specification.UserSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ModelMapper mp;

    @Override
    public UserDTO findByUsernameDTO(String username) {
        return this.mp.map(this.userRepository.findByUsername(username), UserDTO.class);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    //    @Override
//    public boolean checkPassword(UserDetails userDetails, String password) {
//        return this.passwordEncoder.matches(password, userDetails.getPassword());
//    }
//    public boolean checkPassword(UserDetails userDetails, String password) {
//        return userDetails.getPassword().equals(password);
//    }


    @Override
    public boolean checkPassword(UserDetails userDetails, String password) {
        return this.passwordEncoder.matches(password, userDetails.getPassword());
    }


    @Override
    public boolean checkValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
       /* (?=.*[0-9]): Chuỗi phải chứa ít nhất một số.
        (?=.*[a-z]): Chuỗi phải chứa ít nhất một chữ thường.
        (?=.*[A-Z]): Chuỗi phải chứa ít nhất một chữ hoa.
        (?=.*[@#$%^&+=!]): Chuỗi phải chứa ít nhất một ký tự đặc biệt.
        (?=\S+$): Không chứa khoảng trắng.
        .{8,20}: Độ dài của chuỗi từ 8 đến 20 ký tự.*/
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } else {
        }
        return false;
    }

    @Override
    public boolean checkExistUser(String username) {
        UserEntity foundUser = this.userRepository.findByUsername(username);
        if (foundUser != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDTO save(UserEntity user) {
        return this.mp.map(this.userRepository.save(user), UserDTO.class);
    }

    @Override
    public UserEntity findByUsernameOrEmail(String username, String email) {
        Specification<UserEntity> spec = Specification.where(UserSpecification.hasUsernameOrEmail(username, email));
        List<UserEntity> list = userRepository.findAll(spec);
        if (list.size() < 1) return null;
        return list.get(0);
    }

    @Override
    public UserEntity saveGuest(GuestDTO guest) {
        UserEntity user = UserEntity.builder()
                .username(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .fullName(guest.getFullName())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .build();
        return userRepository.save(user);
    }
}
