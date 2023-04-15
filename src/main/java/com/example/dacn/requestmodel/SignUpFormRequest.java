package com.example.dacn.requestmodel;

import lombok.Data;

@Data
public class SignUpFormRequest {
    private String username;
    private String password;
    private String fullName;
    private String phone;
}
