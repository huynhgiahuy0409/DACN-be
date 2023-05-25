package com.example.dacn.services.impl;


import com.example.dacn.entity.DiscountEntity;
import com.example.dacn.repository.AddressRepository;
import com.example.dacn.repository.DiscountRepository;
import com.example.dacn.services.AddressService;
import com.example.dacn.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private DiscountRepository repository;

    @Override
    public DiscountEntity createDiscount(DiscountEntity address) throws Exception {
        return repository.save(address);
    }


}
