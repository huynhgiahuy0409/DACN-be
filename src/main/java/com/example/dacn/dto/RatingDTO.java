package com.example.dacn.dto;

import java.util.Date;

public class RatingDTO {
    private Long id;
    private String content;
    private Double points;

    private String title;

    private Date updateDate;

    private Long hotelId;

    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RatingDTO(Long id, String content, double points, String title, Date updateDate, Long hotelId, String username) {
        this.id = id;
        this.content = content;
        this.points = points;
        this.title = title;
        this.updateDate = updateDate;
        this.hotelId = hotelId;
        this.username = username;
    }

    public RatingDTO() {
    }
}
