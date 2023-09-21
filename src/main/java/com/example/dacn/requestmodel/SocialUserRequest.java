package com.example.dacn.requestmodel;

import com.example.dacn.enums.OAuthProvider;
import lombok.Data;

@Data
public class SocialUserRequest {
    private String email;
    private String name;
    private OAuthProvider provider;
    private String photoUrl;
    private String id;
}
