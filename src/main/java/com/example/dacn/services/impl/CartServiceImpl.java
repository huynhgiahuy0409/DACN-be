package com.example.dacn.services.impl;

import com.example.dacn.dto.request.CartRequest;
import com.example.dacn.dto.response.BenefitResponse;
import com.example.dacn.dto.response.CartResponse;
import com.example.dacn.dto.response.HotelResponse;
import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.enums.RoomStatus;
import com.example.dacn.entity.CartEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.HotelImageEntity;
import com.example.dacn.entity.RoomEntity;
import com.example.dacn.repository.CartRepository;
import com.example.dacn.services.CartService;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.ReservationService;
import com.example.dacn.services.RoomService;
import com.example.dacn.specification.CartSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository repository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<CartResponse> findBySessionId(String sessionId) {
        Specification<CartEntity> spec = Specification.where(CartSpecification.hasSessionId(sessionId));
        return repository.findAll(spec).stream().map(i ->
                CartResponse.builder()
                        .id(i.getId())
                        .adult(i.getAdult())
                        .child(i.getChild())
                        .fromDate(i.getFromDate())
                        .toDate(i.getToDate())
                        .hotel(mapper.map(i.getHotel(), HotelResponse.class))
                        .room(mapper.map(i.getRoom(), RoomResponse.class))
                        .sessionId(i.getSessionId())
                        .address(i.getHotel().getAddress().getProvince().get_name())
                        .bannerImage(findFirstThumbnail(i.getHotel().getHotelImages()))
                        .totalReviews(i.getHotel().getRatings().size())
                        .roomType(i.getRoom().getRoomType().getName())
                        .benefits(i.getRoom().getBenefits().stream().map(item -> mapper.map(item, BenefitResponse.class)).collect(Collectors.toSet()))
                        .status(isReservedBefore(i.getHotel(), i.getRoom(), i.getFromDate(), i.getToDate()))
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public CartResponse addToCart(CartRequest cart) throws Exception {
        Specification<CartEntity> spec = Specification.where(CartSpecification.hasHotelRoomSessionId(cart.getHotelId(), cart.getRoomId(), cart.getSessionId()));
        List<CartEntity> list = repository.findAll(spec);
        if (list.size() > 0) throw new Exception("Phòng này đã tồn tại trong giỏ hàng !");
        HotelEntity hotel = hotelService.findById(cart.getHotelId());
        RoomEntity room = roomService.findByHotelAndRoomId(cart.getHotelId(), cart.getRoomId());
        if (null == hotel || null == room) {
            throw new Exception("Không tìm thấy phòng hợp lệ !");
        }
        if (cart.getAdult() > room.getMaxAdults() || cart.getChild() > room.getMaxChildren())
            throw new Exception("Số lượng người vượt quá ngưỡng cho phép");

        //find reserved list before by hotel id ,room id, from-to date
        List<Long> reservedList = reservationService.findReservationBefore(cart.getHotelId(), cart.getRoomId(), cart.getFromDate(), cart.getToDate());
        if (reservedList.size() > 0) throw new Exception("Phòng đã có khách hàng đặt vui lòng chọn phòng khác !");
        CartEntity cartEntity = CartEntity.builder()
                .adult(cart.getAdult())
                .child(cart.getChild())
                .fromDate(cart.getFromDate())
                .toDate(cart.getToDate())
                .hotel(hotel)
                .room(room)
                .sessionId(cart.getSessionId())
                .build();
        CartEntity addedCartItem = repository.save(cartEntity);
        return mapper.map(addedCartItem, CartResponse.class);
    }

    @Override
    public void deleteCartItemById(Long id) throws Exception {
        repository.deleteById(id);
    }

    @Override
    @Transactional // delete by each id in list
    public void deleteByIds(List<Long> ids) throws Exception {
        repository.deleteByIdIn(ids);
    }

    private String findFirstThumbnail(Set<HotelImageEntity> images) {
        Optional<HotelImageEntity> image = images.stream()
                .filter(item -> item.getIsThumbnail().equals(true))
                .findFirst();
        if (!image.isPresent()) return "";
        return image.get().getUrl();
    }

    private RoomStatus isReservedBefore(HotelEntity hotel, RoomEntity room, LocalDate startDate, LocalDate endDate) {
        try {
            if (null == hotel || null == room) return RoomStatus.UNDEFINED;
            List<Long> reservedList = reservationService.findReservationBefore(hotel.getId(), room.getId(), startDate, endDate);
            if (reservedList.size() > 0)
                return RoomStatus.RESERVED;
            else
                return RoomStatus.AVAILABLE;
        } catch (Exception e) {
            return RoomStatus.UNDEFINED;
        }
    }
}
