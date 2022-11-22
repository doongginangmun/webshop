package com.toy.webshop.entity;


import com.toy.webshop.entity.coupon.UserCoupon;
import com.toy.webshop.entity.discount.DiscountPolicy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)   //연관 관계 주인
    @JoinColumn(name = "delivery_id")                              //매핑할 컬럼이름
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime orderDate;    //주문시간

    private int orderPrice;             //주문금액

    @Enumerated(EnumType.STRING)
    private OrderStatus status;         //주문상태 ORDER, CANCEL


    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private UserCoupon coupon;

    @Transient
    private DiscountPolicy discountPolicy;

    //연관 관계메서드
    public void setUser(User user) {
        this.user = user;
        user.getOrders().add(this);
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    public static Order createOrder(User user, Delivery delivery, DiscountPolicy discountPolicy, OrderItem... orderItems) {
        Order order = new  Order();
        order.setUser(user);
        order.setDelivery(delivery);
        order.setDiscountPolicy(discountPolicy);
        for (OrderItem orderItem : orderItems)
            order.setOrderItem(orderItem);
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderPrice(discountPolicy.calculatePrice(order));

        return order;
    }

    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송이완료된 상품은 취소가불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems)
            orderItem.cancel();
        if(coupon!=null)
            coupon.cancel();
    }

    //할인된 주문 가격 조회
    public int getDiscountOrderPrice() {
       return discountPolicy.calculatePrice(this);
    }

}
