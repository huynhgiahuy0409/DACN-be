package com.example.dacn.model;

import lombok.Data;

@Data
public class CountHotelByPointRange {
    private Object countIn7;
    private Object countIn8;
    private Object countIn9;

    public CountHotelByPointRange(Object countIn7, Object countIn8, Object countIn9) {
        this.countIn7 = countIn7;
        this.countIn8 = countIn8;
        this.countIn9 = countIn9;
    }
}
