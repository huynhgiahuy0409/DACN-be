package com.example.dacn.services;

import com.example.dacn.dto.request.CartRequest;
import com.example.dacn.dto.response.CartResponse;
import com.example.dacn.model.CartEntity;

import java.util.List;

public interface CartService {
    List<CartEntity> findBySessionId(String sessionId);

    CartResponse addToCart(CartRequest cart) throws Exception;

    void deleteCartItemById(Long id) throws Exception;
}
