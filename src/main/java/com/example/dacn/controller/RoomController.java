package com.example.dacn.controller;

import com.example.dacn.entity.RoomEntity;
import com.example.dacn.services.impl.RoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
@CrossOrigin("http://localhost:4200")
public class RoomController {

    @Autowired
    private RoomServiceImpl roomService;

    @GetMapping("/rooms")
    public List<RoomEntity> getAllRoomEntitys() throws Exception {
        return roomService.getAllRoom();

    }


    @PostMapping("/room")
    public RoomEntity createRoom(@RequestBody RoomEntity hp) throws Exception {
        return roomService.createRoom(hp);
    }

    @GetMapping("/rooms/{id}")
    public RoomEntity getRoom(@PathVariable Long id) throws Exception {
        return roomService.getRoom(id);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<RoomEntity> updateRoom(@PathVariable Long id, @RequestBody RoomEntity roomEntity) throws Exception {
        return roomService.updateRoom(id, roomEntity);

    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteRoom(@PathVariable Long id) throws Exception {
        return roomService.deleteRoom(id);
    }
}
