package com.example.dacn.services.impl;

import com.example.dacn.model.AddressEntity;
import com.example.dacn.repository.AddressRepository;
import com.example.dacn.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository repository;

    @Override
    public AddressEntity createAddress(AddressEntity address) throws Exception {
        return repository.save(address);
    }
}
