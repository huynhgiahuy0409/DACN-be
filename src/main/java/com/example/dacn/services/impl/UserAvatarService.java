package com.example.dacn.services.impl;

import com.example.dacn.entity.UserAvatarEntity;
import com.example.dacn.repository.IImageRepository;
import com.example.dacn.repository.IUserAvatarRepository;
import com.example.dacn.services.IUserAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAvatarService implements IUserAvatarService {

    @Autowired
    private IImageRepository imageRepository;

    @Autowired
    private IUserAvatarRepository userAvatarRepository;

    @Override
    public UserAvatarEntity save(UserAvatarEntity userAvatar) {
        return imageRepository.save(userAvatar);
    }

    @Override
    public List<UserAvatarEntity> findAll(Specification spec) {
        return this.userAvatarRepository.findAll(spec);
    }

}
