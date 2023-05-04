package com.example.dacn.requestmodel;

import lombok.Data;

import java.util.Date;

@Data
public class ProductFilterRequest {
    private String search;
    private Date startDate;
    private Date endDate;

    private Integer rooms;

    private Integer adults;

    private Integer children;
    private Long hotelId;

    @Override
    public String toString() {
        return "ProductFilterRequest{" +
                "search='" + search + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", rooms=" + rooms +
                ", adults=" + adults +
                ", children=" + children +
                ", hotelId=" + hotelId +
                '}';
    }
}
