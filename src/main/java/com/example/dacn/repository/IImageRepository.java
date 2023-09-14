package com.example.dacn.repository;

import com.example.dacn.entity.UserAvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<UserAvatarEntity, Long> {

}
