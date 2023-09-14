package com.example.dacn.services;

import com.example.dacn.entity.UserAvatarEntity;
import com.example.dacn.entity.UserCoversEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IUserAvatarService{
    public UserAvatarEntity save(UserAvatarEntity userAvatar);
    public List<UserAvatarEntity> findAll(Specification spec);
}
