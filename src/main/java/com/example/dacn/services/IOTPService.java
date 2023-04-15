package com.example.dacn.services;

public interface IOTPService {
    public int generateOTP(String key);
    public int getOTP(String key);
    public void clearOTP(String key);
}
