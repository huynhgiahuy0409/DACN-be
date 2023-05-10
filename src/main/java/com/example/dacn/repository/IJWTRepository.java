package com.example.dacn.repository;

import com.example.dacn.entity.JWTEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
public interface IJWTRepository extends JpaRepository<JWTEntity, Long> {

    JWTEntity findByToken(String token);

    @Transactional
    Long removeByToken(String token);
}
