package com.toy.webshop.event;

import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.coupon.UserCoupon;

public class CouponEvent {

    private Order order;
    private UserCoupon coupon;

    public CouponEvent(Order order, UserCoupon coupon) {
        this.order = order;
        this.coupon = coupon;
    }

    public Order getOrder() {
        return order;
    }

    public UserCoupon getCoupon() {
        return coupon;
    }
}
