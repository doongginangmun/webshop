package com.toy.webshop.entity.discount;

import com.toy.webshop.entity.Order;

public abstract class AdditionalDiscountPolicy implements DiscountPolicy {

    private DiscountPolicy next;

    public AdditionalDiscountPolicy(DiscountPolicy next) {
        this.next = next;
    }

    public AdditionalDiscountPolicy() {}

    @Override
    public int calculatePrice(Order order) {
        int price = next.calculatePrice(order);
        return afterCalculated(price);
    }

    abstract protected int afterCalculated(int price);
}
