package com.example.dacn.dto.response;

import com.example.dacn.model.BenefitEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class CartResponse {
    private Long id;
    private Integer adult;
    private Integer child;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate toDate;
    private HotelResponse hotel;
    private RoomResponse room;
    private String sessionId;
    private String address;
    private String bannerImage;
    private Integer totalReviews;
    private String roomType;
    private Set<BenefitResponse> benefits;
}
