package com.toy.webshop.dto;

import com.toy.webshop.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {

    private Long itemId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemDto(String itemName, int orderPrice, int count) {
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
    public OrderItemDto(Long itemId, String itemName, int orderPrice, int count) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
    public OrderItemDto(OrderItem orderItem) {
        itemId = orderItem.getItem().getId();
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getItem().getPrice();
        count = orderItem.getCount();
    }
}
