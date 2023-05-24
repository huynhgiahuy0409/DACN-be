package com.example.dacn.services.impl;

import com.example.dacn.dto.GuestDTO;
import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.dto.response.HotelResponse;
import com.example.dacn.dto.response.ReservationResponse;
import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.entity.*;
import com.example.dacn.enums.ReservationStatus;
import com.example.dacn.repository.ReservationRepository;
import com.example.dacn.services.*;
import com.example.dacn.specification.ReservationSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public ReservationResponse cancelReservation(Long id, String username) throws Exception {
        Optional<ReservationEntity> foundReservation = repository.findById(id);
        if (!foundReservation.isPresent() || !foundReservation.get().getUser().getUsername().equals(username))
            throw new Exception("Không tìm thấy đơn đặt phòng tương ứng !");
        ReservationEntity reservation = foundReservation.get();
        reservation.setStatus(ReservationStatus.CANCELLED);
        ReservationEntity updatedReservation = repository.save(reservation);
        return getReservationResponse(updatedReservation);
    }

    @Override
    @Transactional
    public List<ReservationResponse> saveAll(List<ReservationRequest> request) {
        try {
            List<ReservationResponse> responseList;
            List<ReservationEntity> upcomingList = getUpcomingSaveReservationList(request);
            if (upcomingList.size() < 1) throw new Exception("No reservation is valid !");
            List<ReservationEntity> savedList = repository.saveAll(upcomingList);
            responseList = savedList.stream().map(this::getReservationResponse).collect(Collectors.toList());
            emailService.sendReservationAllMail(request.get(0).getFullName(), request.get(0).getEmail());
            return responseList;
        } catch (Exception e) {
            return null;
        }
    }

    private ReservationResponse getReservationResponse(ReservationEntity reservation) {
        return ReservationResponse.builder()
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
                .build();
    }

    public List<ReservationEntity> getUpcomingSaveReservationList(List<ReservationRequest> requestList) throws Exception {
        List<ReservationEntity> re = new ArrayList<>();

        for (ReservationRequest request : requestList) {
            HotelEntity hotel = hotelService.findById(request.getHotelId());

            RoomEntity room = roomService.findByHotelAndRoomId(request.getHotelId(), request.getRoomId());

            List<Long> reservedList = findReservationBefore(request.getHotelId(), request.getRoomId(), request.getStartDate(), request.getEndDate());

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
            if (null != hotel && null != room && request.getAdult() <= room.getMaxAdults()
                    && request.getChildren() <= room.getMaxChildren() && reservedList.size() == 0) {
                Double discountPercent;
                if (ObjectUtils.isEmpty(room.getDiscount()) || room.getDiscount().getDiscountPercent() <= 0)
                    discountPercent = null;
                else
                    discountPercent = room.getDiscount().getDiscountPercent();
                ReservationEntity reservation = ReservationEntity.builder()
                        .adult(request.getAdult())
                        .discountPercent(request.getDiscountPercent())
                        .price(getPriceByNights(request.getStartDate(), request.getEndDate(), room.getOriginPrice(), discountPercent))
                        .children(request.getChildren())
                        .endDate(request.getEndDate())
                        .startDate(request.getStartDate())
                        .hotel(hotel)
                        .room(room)
                        .user(existedUser)
                        .status(ReservationStatus.PENDING)
                        .build();
                re.add(reservation);
            }
        }
        return re;
    }

    private double getPriceByNights(LocalDate startDate, LocalDate endDate, Double price, Double discount) {
        if (discount > 0) {
            double priceBeforeDiscount = calculateNumberOfNights(startDate, endDate) * price;
            double discountAmount = priceBeforeDiscount * (discount / 100);
            return priceBeforeDiscount - discountAmount;
        } else {
            return calculateNumberOfNights(startDate, endDate) * price;
        }
    }

    private int calculateNumberOfNights(LocalDate startDate, LocalDate endDate) {
        int numberOfNights = 0;

        while (startDate.isBefore(endDate)) {
            startDate = startDate.plusDays(1);
            numberOfNights++;
        }

        return numberOfNights;
    }

}
