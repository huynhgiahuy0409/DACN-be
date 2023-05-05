package com.example.dacn.services.impl;

import com.example.dacn.model.HotelEntity;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.responsemodel.AverageRatingResponse;
import com.example.dacn.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository repository;


    @Override
    public HotelEntity findById(Long id) throws Exception {
        Optional<HotelEntity> hotel = repository.findById(id);
        if (!hotel.isPresent()) throw new Exception("Khách sạn không tồn tại !");
        return hotel.get();
    }

    @Override
    @Transactional
    public List<HotelEntity> findAll(Specification specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable).getContent();
    }

    @Override
    @Transactional
    public List<HotelEntity> findAll(Specification specification) {
        return this.repository.findAll(specification);
    }

    @Override
    public List<HotelEntity> findAll(Specification specification, Sort sort) {
        return this.repository.findAll(specification, sort);
    }

    @Override
    public HotelEntity findOne(Specification<HotelEntity> spec) {
        return this.repository.findOne(spec).get();
    }

    @Override
    public HotelEntity findOne(Long id) {
        return this.repository.getOne(id);
    }

    @Override
    public Double computeStarRating(Double hotelPoints) {
        return (hotelPoints * 5) / 10;
    }

    @Override
    public AverageRatingResponse getAverageRatingResponse(HotelEntity hotel) {
        AverageRatingResponse averageRatingResponse = new AverageRatingResponse();
        Double points = hotel.getAveragePoints();
        averageRatingResponse.setPoints(points);
        averageRatingResponse.setReviews(hotel.getRatings().size());
        String name;

        if(points >= 9){
            name = "Trên cả tuyệt với";
        }else if(points >= 8 && points <9){
            name = "Tuyệt vời";
        }
        else if(points >= 7 && points <8){
            name = "Rất tốt";
        }else if(points >= 6 && points <7){
            name = "Hài lòng";
        }else{
            name = "Bình thường";
        }
        averageRatingResponse.setName(name);
        return averageRatingResponse;
    }

}
