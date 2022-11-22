package com.toy.webshop.dto;

import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class OrderDto {

    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private int orderPrice;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Long id, LocalDateTime orderDate, OrderStatus orderStatus,int orderPrice, Address address) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderPrice = orderPrice;
        this.address = address;
    }

    public OrderDto(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.orderPrice = order.getOrderPrice();
        this.address = order.getDelivery().getAddress();
    }
}
