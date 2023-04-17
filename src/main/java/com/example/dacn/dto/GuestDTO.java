package com.example.dacn.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GuestDTO {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
}

