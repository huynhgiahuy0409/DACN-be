package com.example.dacn.repository;


import com.example.dacn.entity.UserCoversEntity;
import com.example.dacn.enums.ImageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserCoverRepository extends JpaRepository<UserCoversEntity, Long> {
    public UserCoversEntity findByUserUsernameAndStatus(String userId, ImageStatus status);
}
