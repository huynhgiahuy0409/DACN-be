package com.example.dacn.services.impl;

import com.example.dacn.dto.request.CartRequest;
import com.example.dacn.dto.response.CartResponse;
import com.example.dacn.model.CartEntity;
import com.example.dacn.model.HotelEntity;
import com.example.dacn.model.RoomEntity;
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

import java.util.List;
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
        return repository.findAll(spec).stream().map(i -> mapper.map(i, CartResponse.class)).collect(Collectors.toList());
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
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new Exception("Xóa thất bại !");
        }
    }
}
