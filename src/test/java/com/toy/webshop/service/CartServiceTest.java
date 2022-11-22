package com.toy.webshop.service;

import com.toy.webshop.dto.CartDto;
import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.item.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    CartService cartService;
    @Autowired
    EntityManager em;


    @Test
    @DisplayName("myCartList 테스트")
    public void testcase() {
        //given
        User user = getUser();
        em.persist(user);
        Book book1 = createBook("시골 JPA", 10000, 10);
        Book book2 = createBook("도시 JPA", 20000, 30);
        em.persist(book1);
        em.persist(book2);

        //when
        cartService.putInCart(book1.getId(), 3, user);
        Long cartId = cartService.putInCart(book2.getId(), 4, user);

        //then
        CartDto cartDto = cartService.myCartList(user.getId());
        System.out.println("cart.items.size() =" +cartDto.getCartItems().size());
        Assertions.assertThat(cartDto.getCartItems().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("deleteCartItem")
    public void deleteCartItem() {
        //given
        User user = getUser();
        em.persist(user);
        Book book1 = createBook("시골 JPA", 10000, 10);
        Book book2 = createBook("도시 JPA", 20000, 30);
        em.persist(book1);
        em.persist(book2);

        //when
        cartService.putInCart(book1.getId(),2, user);
        cartService.putInCart(book2.getId(),2, user);
        List<Long> bookIds = new ArrayList<>();
        bookIds.add(book1.getId());
        bookIds.add(book2.getId());

        //then
        CartDto cartDto1 = cartService.myCartList(user.getId());
        Assertions.assertThat(cartDto1.getCartItems().size()).isEqualTo(2);
        cartService.deleteCartItem(user.getId(), bookIds);
        CartDto cartDto2 = cartService.myCartList(user.getId());
        Assertions.assertThat(cartDto2.getCartItems().size()).isEqualTo(0);

    }

    private User getUser() {
        User user = User.builder()
                .email("kdjkmit@naver.com")
                .password("1234")
                .name("김두한")
                .address(new Address("시티","거리","집코드"))
                .build();
        em.persist(user);
        return user;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        return Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();

    }

}