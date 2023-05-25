package com.example.dacn.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime dob;
    //    @Enumerated(EnumType.ORDINAL)
//    private Gender gender;
    private String phone;
//    @Enumerated(EnumType.ORDINAL)
//    private UserStatus status;
}
