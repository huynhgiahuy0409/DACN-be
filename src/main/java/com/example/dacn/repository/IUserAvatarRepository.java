package com.example.dacn.repository;


import com.example.dacn.entity.UserAvatarEntity;
import com.example.dacn.enums.ImageStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserAvatarRepository extends JpaRepository<UserAvatarEntity, Long> {

    public UserAvatarEntity findByUserUsernameAndStatus(String userId, ImageStatus status);

    public List<UserAvatarEntity> findAll(Specification spec);
}
