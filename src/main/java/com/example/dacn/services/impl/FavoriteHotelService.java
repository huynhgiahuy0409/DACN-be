package com.example.dacn.services.impl;

import com.example.dacn.entity.FavoriteHotelEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.UserEntity;
import com.example.dacn.repository.IFavoriteHotelRepository;
import com.example.dacn.requestmodel.SaveFavoriteHotelRequest;
import com.example.dacn.responsemodel.FavoriteHotelResponse;
import com.example.dacn.services.HotelService;
import com.example.dacn.services.IFavoriteHotelService;
import com.example.dacn.services.IHotelImageService;
import com.example.dacn.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteHotelService implements IFavoriteHotelService {
    @Autowired
    private IFavoriteHotelRepository favoriteHotelRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private UserService userService;
    @Autowired
    private IHotelImageService imageService;

    @Override
    public List<FavoriteHotelResponse> findAllByUsername(String username) {
        return favoriteHotelRepository.findAllByUserUsername(username).stream().map(
                i -> FavoriteHotelResponse.builder()
                        .id(i.getId())
                        .hotelId(i.getHotel().getId())
                        .name(i.getHotel().getName())
                        .bannerUrl(imageService.findFirstBannerImage(i.getHotel().getId()))
                        .address(i.getHotel().getAddress().getWard().get_name() + " ," + i.getHotel().getAddress().getProvince().get_name())
                        .originPrice(roomService.findMinimumPriceRoom(i.getHotel().getId()).getOriginPrice())
                        .finalPrice(roomService.findMinimumPriceRoom(i.getHotel().getId()).getFinalPrice())
                        .avgRating(i.getHotel().getAveragePoints())
                        .totalRating(i.getHotel().getRatings().size())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public FavoriteHotelResponse save(SaveFavoriteHotelRequest request) throws Exception {
        HotelEntity hotel = hotelService.findById(request.getHotelId());
        if (hotel == null) throw new Exception("Không tìm thấy khách sạn tương ứng !");
        UserEntity user = userService.findByUsername(request.getUsername());
        if (user == null) throw new Exception("Không tìm thấy người dùng tương ứng !");
        FavoriteHotelEntity existedItem = favoriteHotelRepository.findFirstByUserUsernameAndHotelId(request.getUsername(), request.getHotelId());
        if (existedItem != null) {
            favoriteHotelRepository.delete(existedItem);
            return FavoriteHotelResponse.builder()
                    .id(hotel.getId())
                    .name(existedItem.getHotel().getName())
                    .bannerUrl(imageService.findFirstBannerImage(existedItem.getHotel().getId()))
                    .address(existedItem.getHotel().getAddress().getWard().get_name() + " ," + existedItem.getHotel().getAddress().getProvince().get_name())
                    .originPrice(roomService.findMinimumPriceRoom(existedItem.getHotel().getId()).getOriginPrice())
                    .finalPrice(roomService.findMinimumPriceRoom(existedItem.getHotel().getId()).getFinalPrice())
                    .avgRating(existedItem.getHotel().getAveragePoints())
                    .totalRating(existedItem.getHotel().getRatings().size())
                    .build();
        }
//            throw new Exception("Khách sạn này đã có trong danh sách yêu thích");
        FavoriteHotelEntity favoriteHotel = FavoriteHotelEntity.builder()
                .hotel(hotel)
                .user(user)
                .build();

        FavoriteHotelEntity savedFavoriteHotel = favoriteHotelRepository.save(favoriteHotel);

        return FavoriteHotelResponse.builder()
                .id(hotel.getId())
                .name(savedFavoriteHotel.getHotel().getName())
                .bannerUrl(imageService.findFirstBannerImage(savedFavoriteHotel.getHotel().getId()))
                .address(savedFavoriteHotel.getHotel().getAddress().getWard().get_name() + " ," + savedFavoriteHotel.getHotel().getAddress().getProvince().get_domain())
                .originPrice(roomService.findMinimumPriceRoom(savedFavoriteHotel.getHotel().getId()).getOriginPrice())
                .finalPrice(roomService.findMinimumPriceRoom(savedFavoriteHotel.getHotel().getId()).getFinalPrice())
                .avgRating(savedFavoriteHotel.getHotel().getAveragePoints())
                .totalRating(savedFavoriteHotel.getHotel().getRatings().size())
                .build();
    }

    @Override
    public Long delete(Long id) throws Exception {
        Optional<FavoriteHotelEntity> foundFavoriteHotel = favoriteHotelRepository.findById(id);
        if (!foundFavoriteHotel.isPresent())
            throw new Exception("Không tìm thấy khách sạn yêu thích trong danh sách !");
        favoriteHotelRepository.delete(foundFavoriteHotel.get());
        return foundFavoriteHotel.get().getId();
    }
}
