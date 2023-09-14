package com.example.dacn.repository;

import com.example.dacn.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
    UserEntity findByUsername(String username);


}