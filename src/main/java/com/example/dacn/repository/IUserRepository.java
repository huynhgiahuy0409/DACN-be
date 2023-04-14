package com.example.dacn.repository;

import com.example.dacn.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);


}