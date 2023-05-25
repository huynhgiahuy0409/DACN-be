package com.example.dacn.controller;

import com.example.dacn.model.AddressEntity;
import com.example.dacn.services.impl.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@CrossOrigin("http://localhost:4200")
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;

    @PostMapping("/address")
    public AddressEntity createAddress(@RequestBody AddressEntity hp) throws Exception {
        return addressService.createAddress(hp);
    }

}
