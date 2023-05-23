package com.example.dacn.services.impl;

import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.ProvinceEntity;
import com.example.dacn.model.ValidSearchedProduct;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.requestmodel.OptionFilterRequest;
import com.example.dacn.requestmodel.ProductFilterRequest;
import com.example.dacn.requestmodel.ProductSortRequest;
import com.example.dacn.responsemodel.AverageRatingResponse;
import com.example.dacn.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;

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
    public HotelEntity getHotel(Long id) throws Exception {
        HotelEntity h = repository.findById(id).orElseThrow(() -> new Exception("Hotel not found" + id));
        return ResponseEntity.ok(h).getBody();

    }

    @Override
    public HotelEntity getOne(Long id) {
        return this.repository.getOne(id);
    }

    @Override
    public HotelEntity getOne(Specification<HotelEntity> specification) {
        return this.repository.findOne(specification).get();
    }

    @Override
    public List<HotelEntity> getAllHotel() throws Exception {
        return repository.findAll();
    }

    @Override
    public HotelEntity createHotel(HotelEntity hotel) throws Exception {
        return repository.save(hotel);
    }

    @Override
    public ResponseEntity<HotelEntity> updateHotel(Long id, HotelEntity hotel) throws Exception {
        HotelEntity h = repository.findById(id).orElseThrow(() -> new Exception("Hotel not found" + id));
        h.setHotelImages(hotel.getHotelImages());
        h.setName(hotel.getName());
        h.setAddress(hotel.getAddress());

        h.setDescription(hotel.getDescription());
        h.setOwner(hotel.getOwner());
        h.setFacilities(hotel.getFacilities());
        h.setRatings(hotel.getRatings());
        h.setAveragePoints(hotel.getAveragePoints());
        h.setStatus(hotel.getStatus());
        h.setRooms(hotel.getRooms());
        h.setReservations(hotel.getReservations());
        h.setCartItems(hotel.getCartItems());
        HotelEntity re = repository.save(h);
        return ResponseEntity.ok(re);
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteHotel(Long id) throws Exception {
        HotelEntity h = repository.findById(id).orElseThrow(() -> new Exception("Hotel not found" + id));
        repository.delete(h);
        Map<String, Boolean> re = new HashMap<>();
        re.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(re);
    }

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

        if (points >= 9) {
            name = "Trên cả tuyệt với";
        } else if (points >= 8 && points < 9) {
            name = "Tuyệt vời";
        } else if (points >= 7 && points < 8) {
            name = "Rất tốt";
        } else if (points >= 6 && points < 7) {
            name = "Hài lòng";
        } else {
            name = "Bình thường";
        }
        averageRatingResponse.setName(name);
        return averageRatingResponse;
    }

    @Override
    public ValidSearchedProduct findSearchedProductItem(ProductFilterRequest productFilterRequest) {
        List<Long> hotelFacilities = Arrays.asList();
        List<Long> benefits = Arrays.asList();
        Integer checkHotelFacilityIds = 0;
        Integer checkBenefits = 0;
        OptionFilterRequest optionFilterRequest = productFilterRequest.getOptionFilter();
        if (optionFilterRequest != null && optionFilterRequest.getHotelFacilities() != null && optionFilterRequest.getHotelFacilities().size() > 0) {
            hotelFacilities = optionFilterRequest.getHotelFacilities();
            checkHotelFacilityIds = optionFilterRequest.getHotelFacilities().size();
        }
        if (optionFilterRequest != null && optionFilterRequest.getBenefits() != null && optionFilterRequest.getBenefits().size() > 0) {
            benefits = optionFilterRequest.getBenefits();
            checkBenefits = optionFilterRequest.getBenefits().size();
        }
        System.out.println("valid hotel " + productFilterRequest.getValue());
        Object result = this.repository.findValidSearchedProduct(productFilterRequest.getValue(), productFilterRequest.getOptionFilter().getGuestRating(), productFilterRequest.getOptionFilter().getDiscount(), checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), productFilterRequest.getAdults(), productFilterRequest.getChildren());
        if (result != null) {
            Object[] responses = Arrays.stream((Object[]) result).map(o -> {
                return o.toString();
            }).toArray(Object[]::new);
            return new ValidSearchedProduct(Long.parseLong(responses[0].toString()), Long.parseLong(responses[1].toString()));
        } else {
            return null;
        }
    }

    @Override
    public List<Object> findRelativeProductItem(ProductFilterRequest productFilterRequest) {
        Long hotelId = productFilterRequest.getValue();
        HotelEntity searchedHotel = this.getOne(hotelId);
        ProvinceEntity province = searchedHotel.getAddress().getProvince();
        Pageable p = PageRequest.of(0, 20);
        List<Object> result;
        String dir;
        String orderBy;
        ProductSortRequest productSort = productFilterRequest.getProductSort();
        if (productSort != null) {
            dir = productFilterRequest.getProductSort().getDirection();
            orderBy = productSort.getProperty();
        } else {
            dir = "desc";
            orderBy = "";
        }
        List<Long> hotelFacilities = Arrays.asList();
        List<Long> benefits = Arrays.asList();
        Integer checkHotelFacilityIds = 0;
        Integer checkBenefits = 0;
        OptionFilterRequest optionFilterRequest = productFilterRequest.getOptionFilter();
        if (optionFilterRequest != null && optionFilterRequest.getHotelFacilities() != null && optionFilterRequest.getHotelFacilities().size() > 0) {
            hotelFacilities = optionFilterRequest.getHotelFacilities();
            checkHotelFacilityIds = optionFilterRequest.getHotelFacilities().size();
        }
        if (optionFilterRequest != null && optionFilterRequest.getBenefits() != null && optionFilterRequest.getBenefits().size() > 0) {
            benefits = optionFilterRequest.getBenefits();
            checkBenefits = optionFilterRequest.getBenefits().size();
        }
        result = this.repository.findValidRelativeSearchedProduct(productFilterRequest.getOptionFilter().getGuestRating(), productFilterRequest.getOptionFilter().getDiscount(), checkHotelFacilityIds, hotelFacilities, checkBenefits, benefits, productFilterRequest.getOptionFilter().getPriceFrom(), productFilterRequest.getOptionFilter().getPriceTo(), province.getId(), productFilterRequest.getAdults(), productFilterRequest.getChildren(), orderBy, dir, p);
        return result;
    }

}
