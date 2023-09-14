package com.example.dacn.services;


import com.example.dacn.entity.UserCoversEntity;
import com.example.dacn.enums.ImageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface IUserCoverService {
    public UserCoversEntity save(UserCoversEntity userCoversEntity);
    UserCoversEntity findByUsernameAndStatus(String userId, ImageStatus status);

}
