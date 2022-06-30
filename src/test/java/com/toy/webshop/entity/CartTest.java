package com.toy.webshop.entity;

import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.repository.CartRepository;
import com.toy.webshop.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CartTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;

    @Test
    @DisplayName("장바구니 조회")
    public void testcase1() {
        //given
        User user = createUser();
        Cart cart = new Cart();

        //when
        userRepository.save(user);
        cart.setUser(user);
        cartRepository.save(cart);

        //then
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(NotExistItemException::new);
        assertEquals(user.getId(), savedCart.getUser().getId());
    }

    private User createUser() {
        User user = User.builder()
                .name("김두한")
                .password("1234")
                .email("kim@naver.com")
                .address(new Address("서울시","강동구","113"))
                .build();
        return user;
    }

}