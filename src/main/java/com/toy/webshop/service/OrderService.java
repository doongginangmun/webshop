package com.toy.webshop.service;

import com.toy.webshop.dto.OrderDto;
import com.toy.webshop.dto.OrderItemDto;
import com.toy.webshop.entity.*;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.entity.discount.NonDiscountPolicy;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.exception.NotFoundUserException;
import com.toy.webshop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartService cartService;
    private final CouponRepository couponRepository;
    private final UserCouponService userCouponService;

    /**
     * 주문 - 쿠폰 할인
     */
    @Transactional
    public Long orderCoupon(User user, int count, Long couponId, List<Long> itemId) {

        List<UserCoupon> myCoupon = userCouponService.findMyCoupon(user);
        UserCoupon userCoupon = myCoupon.stream().filter(m -> m.getCoupon().getId().equals(couponId))
                .findAny().orElseThrow(NotExistItemException::new);

        Coupon findCoupon = couponRepository.findById(couponId)
                .orElseThrow(NotExistItemException::new);

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(NotFoundUserException::new);

        List<Item> collect = itemId.stream().map(id ->
                    itemRepository.findById(id).orElseThrow(NotExistItemException::new))
                    .collect(Collectors.toList());

        OrderItem[] orderItems = collect.stream().map(i ->
                    OrderItem.createOrderItem(i, i.getPrice(), count))
                    .toArray(OrderItem[]::new);

        Delivery delivery = new Delivery();
        delivery.setAddress(findUser.getAddress());

        //DiscountPolicy 인자로 findCoupon 전달
        Order order = Order.createOrder(findUser, delivery, findCoupon, orderItems);

        orderRepository.save(order);
        userCoupon.setOrder(order);
        userCoupon.usedCoupon(); //[used :true, quantity -1]
        cartService.deleteCartItem(findUser.getId(), itemId);
        return order.getId();
    }

    /**
     * 주문 - 쿠폰 할인X
     */
    @Transactional
    public Long order(User user, int count, List<Long> itemId) {

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(NotFoundUserException::new);

        List<Item> collect = itemId.stream().map(id ->
                itemRepository.findById(id).orElseThrow(NotExistItemException::new))
                .collect(Collectors.toList());

        OrderItem[] orderItems = collect.stream().map(i ->
                OrderItem.createOrderItem(i, i.getPrice(), count))
                .toArray(OrderItem[]::new);

        Delivery delivery = new Delivery();
        delivery.setAddress(findUser.getAddress());

        Order order = Order.createOrder(findUser, delivery, new NonDiscountPolicy(), orderItems);

        orderRepository.save(order);
        cartService.deleteCartItem(findUser.getId(), itemId);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        //주문 조회
        Order result = orderRepository.findById(orderId).orElseThrow(IllegalStateException::new);
        //주문 취소
        result.cancel();

    }

    @Transactional
    public List<OrderDto> orderList(Long userId) {
        //Order와 toOne 관계인 컬럼들만 먼저 조인
        List<OrderDto> result = orderRepository.findOrders(userId);

        //Order와 toMany 관계인 OrderItem만 따로 조회 한 후 Order에 넣어준다.
        result.forEach(o-> {
            List<OrderItemDto> orderItems = orderRepository.findOrderItems(o.getId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

}
