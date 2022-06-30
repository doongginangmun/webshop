package com.toy.webshop.entity.discount;


import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.OrderItem;

public class NonDiscountPolicy implements DiscountPolicy{

    @Override
    public int calculatePrice(Order order) {
        return order.getOrderItems().stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
