package com.example.dacn.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "room_image")
@Getter
@Setter
public class RoomImageEntity extends ImageBase{

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;
}
