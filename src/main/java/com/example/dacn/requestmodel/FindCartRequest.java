package com.example.dacn.requestmodel;

import lombok.Data;

@Data
public class FindCartRequest {
    private String sessionId;
    private Long roomId;
}
