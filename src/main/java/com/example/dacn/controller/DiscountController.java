package com.example.dacn.controller;



import com.example.dacn.entity.DiscountEntity;
import com.example.dacn.services.impl.DiscountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discount")
@CrossOrigin("http://localhost:4200")
public class DiscountController {
    @Autowired
    private DiscountServiceImpl addressService;

    @PostMapping("/discount")
    public DiscountEntity createDiscount(@RequestBody DiscountEntity hp) throws Exception {
        return addressService.createDiscount(hp);
    }
}
