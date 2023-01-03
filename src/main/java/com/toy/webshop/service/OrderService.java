package com.toy.webshop.service;

import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.dto.OrderDetailDto;
import com.toy.webshop.dto.OrderDto;
import com.toy.webshop.dto.OrderItemDto;
import com.toy.webshop.entity.Delivery;
import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.OrderItem;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.entity.discount.NonDiscountPolicy;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.entity.point.Point;
import com.toy.webshop.event.CartEvent;
import com.toy.webshop.event.CouponEvent;
import com.toy.webshop.exception.NotExistItemException;
import com.toy.webshop.exception.NotFoundUserException;
import com.toy.webshop.repository.ItemRepository;
import com.toy.webshop.repository.OrderRepository;
import com.toy.webshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserCouponService userCouponService;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 주문 - 쿠폰 할인
     */
    @Transactional
    public Long orderCoupon(User user, List<ItemDto> itemDtos, List<Long> itemIds, Long couponId, int point) {
        List<UserCoupon> myCoupon = userCouponService.findMyCoupon(user);

        UserCoupon userCoupon = myCoupon.stream()
                        .filter(m -> m.getCoupon().getId().equals(couponId))
                        .findAny().orElseThrow(NotExistItemException::new);

        if(isExpired(userCoupon))
            throw new IllegalStateException("쿠폰의 날짜가 만료되었습니다.");

        User findUser = userRepository.findById(user.getId())
                        .orElseThrow(NotFoundUserException::new);

        List<Item> collect = itemIds.stream().map(id ->
                        itemRepository.findById(id)
                                .orElseThrow(NotExistItemException::new))
                        .collect(Collectors.toList());

        OrderItem[] orderItems = collect.stream().map(i ->
                        OrderItem.createOrderItem(i, i.getPrice(),
                                itemDtos.stream()
                                        .filter(x -> x.getId().equals(i.getId()))
                                        .findFirst().orElseThrow(NotExistItemException::new)
                                        .getCount()))
                        .toArray(OrderItem[]::new);

        Delivery delivery = new Delivery();
        delivery.setAddress(findUser.getAddress());

        Point p = Point.createUsePoint(userCoupon.getCoupon(), user, point);
        if(point != 0) applicationEventPublisher.publishEvent(p); //포인트 사용

        //DiscountPolicy 인자로 Coupon을 가진 Point 인스턴스 전달
        Order order = Order.createOrder(findUser, delivery, p, orderItems);

        orderRepository.save(order);

        applicationEventPublisher.publishEvent(new CartEvent(order, findUser, itemIds)); //아이템 카트에서 제거
        applicationEventPublisher.publishEvent(new CouponEvent(order, userCoupon)); //쿠폰 사용처리
        if(point == 0) applicationEventPublisher.publishEvent(order); //point 적립
        return order.getId();
    }

    /**
     * 주문 - 쿠폰 할인X
     */
    @Transactional
    public Long order(User user, List<ItemDto> itemDtos, List<Long> itemIds, int point) {

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(NotFoundUserException::new);

        List<Item> collect = itemIds.stream().map(id ->
                itemRepository.findById(id).orElseThrow(NotExistItemException::new))
                .collect(Collectors.toList());

        OrderItem[] orderItems = collect.stream().map(i ->
                OrderItem.createOrderItem(i, i.getPrice(), itemDtos.stream()
                                .filter(x -> x.getId().equals(i.getId()))
                                .findFirst().orElseThrow(NotExistItemException::new)
                                .getCount()))
                .toArray(OrderItem[]::new);

        Delivery delivery = new Delivery();
        delivery.setAddress(findUser.getAddress());

        Point p = Point.createUsePoint(new NonDiscountPolicy(), findUser, point);
        if(point != 0) applicationEventPublisher.publishEvent(p);

        Order order = Order.createOrder(findUser, delivery, p, orderItems);

        orderRepository.save(order);

        applicationEventPublisher.publishEvent(new CartEvent(order, findUser, itemIds)); //구매한 아이템 카트에서 제거
        if(point == 0) applicationEventPublisher.publishEvent(order); //point 적립
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

    public OrderDetailDto findOrder(Long orderId) {
        Order result = orderRepository.findById(orderId)
                .orElseThrow(NotExistItemException::new);
        List<OrderItemDto> orderItems = orderRepository.findOrderItems(orderId);
        OrderDetailDto orderDetailDto = new OrderDetailDto(result);
        orderDetailDto.setOrderItems(orderItems);
        return orderDetailDto;
    }

    private boolean isExpired(UserCoupon userCoupon) {
        LocalDateTime expired_at = userCoupon.getExpired_at();
        LocalDateTime now = LocalDateTime.now();
        return expired_at.isBefore(now);
    }
}
