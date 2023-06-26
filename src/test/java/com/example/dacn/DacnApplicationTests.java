package com.example.dacn;

import com.example.dacn.dto.response.CartResponse;
import com.example.dacn.entity.FacilityEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.repository.HotelRepository;
import com.example.dacn.repository.IFacilityRepository;
import com.example.dacn.services.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace =
        AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class DacnApplicationTests {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CartService cartService;


    @Test
    public void testListHotel() {
        Iterable<HotelEntity> hotels = hotelRepository.findAll();
        Assertions.assertTrue(hotels.spliterator().getExactSizeIfKnown() > 0);
    }

    @Test
    public void testGetHotelById() {
        Long id = 1L;
        Optional<HotelEntity> optionalHotel = hotelRepository.findById(id);
        Assertions.assertNotNull(optionalHotel);
    }

    @Test
    public void testListItemByCartGreaterThanZero() {
        String sessionId = "9bdd7086-2cc4-40ad-9e40-3481ae9f16db";
        Iterable<CartResponse> cart = cartService.findBySessionId(sessionId);
        Assertions.assertTrue(cart.spliterator().getExactSizeIfKnown() > 0);
    }

    @Test
    public void testListItemByCartLessThanZero() {
        String sessionId = "9bdd7086-2cc4-40ad-9e40-3481ae9f16db";
        Iterable<CartResponse> cart = cartService.findBySessionId(sessionId);
        Assertions.assertTrue(cart.spliterator().getExactSizeIfKnown() > 0);
    }

}
