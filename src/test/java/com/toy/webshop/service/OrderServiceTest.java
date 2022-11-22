package com.toy.webshop.service;

import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.*;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.CouponType;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.repository.CouponRepository;
import com.toy.webshop.repository.ItemRepository;
import com.toy.webshop.repository.OrderRepository;
import com.toy.webshop.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    UserCouponService userCouponService;
    @Autowired
    EntityManager em;


    @Test
    @DisplayName("상품 주문")
    public void order2() {
        //given
        User user = getUser();
        Item item1 = createBook("시골 JPA", 10000, 10);
        Item item2 = createBook("김두한 JPA", 20000, 20);
        putInCart(user, item1, item2);
        List<ItemDto> itemDtos = List.of(item1, item2)
                .stream()
                .map(ItemDto::new).collect(Collectors.toList());

        List<Long> itemIds = itemDtos.stream()
                .map(ItemDto::getId).collect(Collectors.toList());
        //When
        Long order = orderService.order(user, itemDtos, itemIds);

        //Then
        Optional<Order> findOrder = orderRepository.findById(order);
        assertThat(findOrder.get().getOrderItems()).extracting("item").extracting("name").containsExactly("시골 JPA", "김두한 JPA");
        assertThat(findOrder.get().getOrderPrice()).isEqualTo(30000);
    }

    @Test
    @DisplayName("주문-쿠폰테스트")
    public void orderCoupon() {
        //given
        User user = getUser();
        Item item1 = createBook("시골 JPA", 10000, 10);
        Item item2 = createBook("김두한 JPA", 20000, 20);
        putInCart(user, item1, item2);
        List<ItemDto> itemDtos = List.of(item1, item2)
                .stream()
                .map(ItemDto::new).collect(Collectors.toList());

        List<Long> itemIds = itemDtos.stream()
                .map(ItemDto::getId).collect(Collectors.toList());
        Coupon coupon = Coupon.createCoupon("20000원할인쿠폰", 20000, 1, CouponType.FIXED);

        Coupon savedCoupon = couponRepository.save(coupon);
        userCouponService.save(user, savedCoupon.getId());
        putInCart(user, item1, item2);
        //when
        Long savedOrder = orderService.orderCoupon(user, itemDtos, itemIds, savedCoupon.getId());
        Order findOrder = orderRepository.findById(savedOrder).get();

        //then
        assertThat(findOrder.getOrderPrice()).isEqualTo(10000);
        assertThat(findOrder.getCoupon().getCoupon().getName()).isEqualTo("20000원할인쿠폰");
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

    private Item createBook(String name, int price, int stockQuantity) {
        Book book = Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
        em.persist(book);
        return book;
    }
    private void putInCart(User user, Item... items) {
        for(Item item : items)
            cartService.putInCart(item.getId(),1, user);
    }

}