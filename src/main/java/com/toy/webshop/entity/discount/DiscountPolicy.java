package com.toy.webshop.entity.discount;

import com.toy.webshop.entity.Order;

public interface DiscountPolicy {

    int calculatePrice(Order order);
}
