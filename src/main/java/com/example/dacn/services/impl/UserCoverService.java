package com.example.dacn.services.impl;


import com.example.dacn.entity.UserCoversEntity;
import com.example.dacn.enums.ImageStatus;
import com.example.dacn.repository.IUserCoverRepository;
import com.example.dacn.services.IUserCoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCoverService implements IUserCoverService {
    @Autowired
    private IUserCoverRepository userCoverRepository;

    @Override
    public UserCoversEntity save(UserCoversEntity userCoversEntity) {
        return this.userCoverRepository.save(userCoversEntity);
    }

    @Override
    public UserCoversEntity findByUsernameAndStatus(String userId, ImageStatus status) {
        return this.userCoverRepository.findByUserUsernameAndStatus(userId, status);
    }
}
