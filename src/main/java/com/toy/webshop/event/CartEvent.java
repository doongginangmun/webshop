package com.toy.webshop.event;

import com.toy.webshop.entity.Order;
import com.toy.webshop.entity.User;


import java.util.List;

public class CartEvent {
   private Order order;
   private User findUser;
   private List<Long> itemIds;


    public CartEvent(Order order, User findUser, List<Long> itemIds) {
        this.order = order;
        this.findUser = findUser;
        this.itemIds = itemIds;
    }

    public Order getOrder() {
        return order;
    }

    public User getFindUser() {
        return findUser;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }
}
