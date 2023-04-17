package com.example.dacn.services.impl;

import com.example.dacn.dto.GuestDTO;
import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.dto.response.HotelResponse;
import com.example.dacn.dto.response.ReservationResponse;
import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.enums.ReservationStatus;
import com.example.dacn.model.*;
import com.example.dacn.repository.ReservationRepository;
import com.example.dacn.services.*;
import com.example.dacn.specification.ReservationSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository repository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private IUserService userService;

    @Override
    public ReservationResponse findById(Long id) throws Exception {
        Optional<ReservationEntity> foundReservation = repository.findById(id);
        if (!foundReservation.isPresent()) throw new Exception("Không tìm thấy đơn đặt phòng tương ứng !");

        ReservationEntity reservation = foundReservation.get();
        return ReservationResponse.builder()
                .id(id)
                .price(reservation.getPrice())
                .adult(reservation.getAdult())
                .children(reservation.getChildren())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .discountPercent(reservation.getDiscountPercent())
                .status(reservation.getStatus())
                .room(mapper.map(reservation.getRoom(), RoomResponse.class))
                .hotel(mapper.map(reservation.getHotel(), HotelResponse.class))
                .build();
    }

    @Override
    public List<Long> findReservationBefore(Long hotelId, Long roomId, LocalDate startDate, LocalDate endDate) {
        return repository.findAll(ReservationSpecification.hasReserveBefore(hotelId, roomId, startDate, endDate))
                .stream().map(BaseEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<ReservationResponse> findAllByUsername(String username) {
        return repository.findAll(ReservationSpecification.hasUsername(username))
                .stream()
                .map(reservation -> ReservationResponse.builder()
                        .id(reservation.getId())
                        .price(reservation.getPrice())
                        .adult(reservation.getAdult())
                        .children(reservation.getChildren())
                        .startDate(reservation.getStartDate())
                        .endDate(reservation.getEndDate())
                        .discountPercent(reservation.getDiscountPercent())
                        .status(reservation.getStatus())
                        .room(mapper.map(reservation.getRoom(), RoomResponse.class))
                        .hotel(mapper.map(reservation.getHotel(), HotelResponse.class))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse save(ReservationRequest request) throws Exception {
        HotelEntity hotel = hotelService.findById(request.getHotelId());
        RoomEntity room = roomService.findByHotelAndRoomId(request.getHotelId(), request.getRoomId());
        if (null == hotel || null == room) throw new Exception("Đặt phòng thất bại do phòng không tồn tại !");
        if (request.getAdult() > room.getMaxAdults() || request.getChildren() > room.getMaxChildren())
            throw new Exception("Số lượng người vượt quá ngưỡng cho phép");
        List<Long> reservedList = findReservationBefore(request.getHotelId(), request.getRoomId(), request.getStartDate(), request.getEndDate());
        if (reservedList.size() > 0) throw new Exception("Phòng đã có khách hàng đặt vui lòng chọn phòng khác !");

        UserEntity existedUser = userService.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        if (null == existedUser) {
            GuestDTO guest = GuestDTO.builder()
                    .username(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .build();
            existedUser = userService.saveGuest(guest);
        }

        ReservationEntity reservation = ReservationEntity.builder()
                .adult(request.getAdult())
                .discountPercent(request.getDiscountPercent())
                .price(request.getPrice())
                .children(request.getChildren())
                .endDate(request.getEndDate())
                .startDate(request.getStartDate())
                .hotel(hotel)
                .room(room)
                .user(existedUser)
                .status(ReservationStatus.PENDING)
                .build();
        ReservationEntity savedEntity = repository.save(reservation);
        emailService.sendReservationMail(savedEntity.getId(), request.getEmail(), hotel.getName(),
                hotel.getAddress().getProvince().get_name(), request.getPrice(), request.getStartDate(), request.getEndDate());
        return getReservationResponse(savedEntity);
    }

    @Override
    public ReservationResponse cancelReservation(Long id, String username) throws Exception {
        Optional<ReservationEntity> foundReservation = repository.findById(id);
        if (!foundReservation.isPresent() || !foundReservation.get().getUser().getUsername().equals(username))
            throw new Exception("Không tìm thấy đơn đặt phòng tương ứng !");
        ReservationEntity reservation = foundReservation.get();
        reservation.setStatus(ReservationStatus.CANCELLED);
        ReservationEntity updatedReservation = repository.save(reservation);
        return getReservationResponse(updatedReservation);
    }

    private ReservationResponse getReservationResponse(ReservationEntity updatedReservation) {
        return ReservationResponse.builder()
                .id(updatedReservation.getId())
                .price(updatedReservation.getPrice())
                .adult(updatedReservation.getAdult())
                .children(updatedReservation.getChildren())
                .startDate(updatedReservation.getStartDate())
                .endDate(updatedReservation.getEndDate())
                .discountPercent(updatedReservation.getDiscountPercent())
                .status(updatedReservation.getStatus())
                .room(mapper.map(updatedReservation.getRoom(), RoomResponse.class))
                .hotel(mapper.map(updatedReservation.getHotel(), HotelResponse.class))
                .build();
    }
}
