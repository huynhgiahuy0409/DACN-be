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

    private ProductSortRequest productSortRequest;


}
