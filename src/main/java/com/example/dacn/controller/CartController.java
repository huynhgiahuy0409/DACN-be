package com.example.dacn.controller;

import com.example.dacn.dto.request.CartRequest;
import com.example.dacn.dto.response.CartResponse;
import com.example.dacn.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@CrossOrigin("http://localhost:4200")
public class CartController {
    @Autowired
    private CartService service;

    @GetMapping("/getCartBySessionId/{id}")
    public ResponseEntity<?> getCartBySessionId(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(service.findBySessionId(id));
    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest cart) {
        try {
            CartResponse res = service.addToCart(cart);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteItemFromCart")
    public ResponseEntity<?> deleteItemFromCart(@RequestParam("id") Long id) {
        try {
            service.deleteCartItemById(id);
            return ResponseEntity.ok().body("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa thất bại !");
        }
    }
}