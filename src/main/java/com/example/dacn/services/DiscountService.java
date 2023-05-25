package com.example.dacn.services;


import com.example.dacn.entity.DiscountEntity;
import org.springframework.stereotype.Service;

@Service
public interface DiscountService {
    DiscountEntity createDiscount(DiscountEntity discount) throws Exception;
}
