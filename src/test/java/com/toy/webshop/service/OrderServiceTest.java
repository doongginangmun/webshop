package com.toy.webshop.service;

import com.toy.webshop.entity.*;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.CouponType;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.repository.CouponRepository;
import com.toy.webshop.repository.OrderRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    UserCouponService userCouponService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    CartService cartService;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("한개주문테스트")
    public void order() {
        //given
        User user = getUser();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        //When
        putInCart(user, item);
        Long orderId = orderService.order(user, orderCount, List.of(item.getId()));
        Order findOrder = orderRepository.findById(orderId).get();

        //Then
        assertThat(findOrder.getUser()).isEqualTo(user);
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);

    }
    @Test
    @DisplayName("여러주문테스트")
    public void order2() {
        //given
        User user = getUser();
        Item item1 = createBook("시골 JPA", 10000, 10);
        Item item2 = createBook("김두한 JPA", 20000, 20);
        int orderCount = 2;

        //When
        putInCart(user, item1, item2);
        orderService.order(user, orderCount, Arrays.asList(item1.getId(), item2.getId()));
        List<Order> findOrders = orderRepository.findByUserId(user.getId());

        //Then
        assertThat(findOrders.get(0).getOrderItems().get(0).getItem().getName()).isEqualTo("시골 JPA");
        assertThat(findOrders.get(0).getOrderItems().get(1).getItem().getName()).isEqualTo("김두한 JPA");

    }

    @Test
    @DisplayName("주문-쿠폰테스트")
    public void orderCoupon() {
        //given
        User user = getUser();
        Item item1 = createBook("시골 JPA", 10000, 10);
        Item item2 = createBook("김두한 JPA", 20000, 20);
        int orderCount = 1;

        Delivery delivery = new Delivery();
        delivery.setAddress(user.getAddress());

        Coupon coupon = Coupon.createCoupon("20000원할인쿠폰", 20000, 1,CouponType.FIXED);

        Coupon savedCoupon = couponRepository.save(coupon);
        userCouponService.save(user, savedCoupon.getId());

        //when
        putInCart(user, item1, item2);
        orderService.orderCoupon(user, orderCount, savedCoupon.getId(),
                Arrays.asList(item1.getId(), item2.getId()));

        List<Order> findOrder = orderRepository.findAll();

        //then
        assertThat(findOrder.size()).isEqualTo(1);
        assertThat(findOrder.get(0).getOrderPrice()).isEqualTo(10000);
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
            cartService.putInCart(item.getId(), user);
    }
}