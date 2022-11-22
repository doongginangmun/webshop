package com.toy.webshop.dto;

import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.OrderStatus;
import com.toy.webshop.entity.coupon.Coupon;
import com.toy.webshop.entity.coupon.UserCoupon;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter @Setter
public class OrderDetailDto {

    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private int orderPrice;
    private Address address;
    private List<OrderItemDto> orderItems;
    private UserCoupon coupon;

    public OrderDetailDto(Long id, LocalDateTime orderDate, OrderStatus orderStatus,int orderPrice, Address address) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderPrice = orderPrice;
        this.address = address;
        this.coupon = coupon;
    }

    public OrderDetailDto(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.orderPrice = order.getOrderPrice();
        this.address = order.getDelivery().getAddress();
        this.coupon = order.getCoupon();
    }
}
