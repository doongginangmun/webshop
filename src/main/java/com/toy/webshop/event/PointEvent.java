package com.toy.webshop.event;

import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.User;

public class PointEvent {

    private Order order;
    private User user;

    public PointEvent(Order order, User user) {
        this.order = order;
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public User getUser() {
        return user;
    }
}
