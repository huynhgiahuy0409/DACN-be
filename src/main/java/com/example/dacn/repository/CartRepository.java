package com.example.dacn.repository;

import com.example.dacn.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long>, JpaSpecificationExecutor<CartEntity> {
    void deleteByIdIn(List<Long> ids);
}
