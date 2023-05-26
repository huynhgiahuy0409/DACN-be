package com.example.dacn.requestmodel;
import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String username;
    private String newPassword;
}
