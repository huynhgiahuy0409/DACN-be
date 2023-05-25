package com.example.dacn.dto;

import com.example.dacn.enums.ReservationStatus;

import java.util.Date;

public class ReservationDTO {
    private Long id;
    private Date createdDate;
    private Date modifiedDate;
    private Integer adult;
    private Integer children;

    private Double discountPercent;
    private Double price;
    private ReservationStatus status;
    private Long hotelId;
    private Long roomId;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getAdult() {
        return adult;
    }

    public void setAdult(Integer adult) {
        this.adult = adult;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ReservationDTO() {
    }

    public ReservationDTO(Long id, Date createdDate, Date modifiedDate, Integer adult, Integer children, Double discountPercent, Double price, ReservationStatus status, Long hotelId, Long roomId, String username) {
        this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.adult = adult;
        this.children = children;
        this.discountPercent = discountPercent;
        this.price = price;
        this.status = status;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.username = username;
    }
}
