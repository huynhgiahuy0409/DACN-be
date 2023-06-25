package com.example.dacn.controller;

import com.example.dacn.requestmodel.SaveFavoriteHotelRequest;
import com.example.dacn.responsemodel.APIResponse;
import com.example.dacn.services.IFavoriteHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")

public class FavoriteHotelController {
    @Autowired
    private IFavoriteHotelService favoriteHotelService;

    @GetMapping("/findAllByUsername")
    public ResponseEntity<?> findAllByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(favoriteHotelService.findAllByUsername(username));
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam("id") Long id) {
        try {
            long deletedItemId = favoriteHotelService.delete(id);
            APIResponse response = new APIResponse(deletedItemId, "Xóa thành công !", 202);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SaveFavoriteHotelRequest request) {
        try {
            return ResponseEntity.ok(favoriteHotelService.save(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
