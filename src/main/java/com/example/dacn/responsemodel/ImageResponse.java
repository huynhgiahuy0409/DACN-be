package com.example.dacn.responsemodel;

import lombok.Data;

@Data
public class ImageResponse {
    private Boolean isThumbnail;
    private String url;

    public ImageResponse(Boolean isThumbnail, String url) {
        this.isThumbnail = isThumbnail;
        this.url = url;
    }
}
